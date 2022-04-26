package client;

import collection.CollectionChangelist;
import collection.CollectionFilter;
import commands.*;
import commands.readers.DtoReader;
import commands.readers.IOSource;
import commands.readers.UserReader;
import communication.Exchanger;
import exceptions.InvalidCredentialsException;
import exceptions.ReconnectionTimeoutException;
import exceptions.ScriptExecutionException;
import lombok.Setter;
import model.ModelDto;
import model.builder.BuilderWrapper;
import model.builder.ModelDtoBuilderWrapper;
import model.data.Flat;
import model.data.ModelDtoBuilder;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import user.User;

import java.io.*;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.TreeSet;

/**
 * This class creates interactive prompt, reads user input, checks commands and sends request to manager.
 */
public class UserConsole implements Console, IOSource {
  private final PrintStream out;
  private final CommandManager commandManager;
  private final Exchanger<Flat> exchanger;
  private final CollectionFilter collectionFilter;
  private final BuilderWrapper<ModelDto> builderWrapper;
  private final DtoReader dtoReader;

  // contains username + script name + $
  private String scriptPrompt;


  private final NavigableSet<String> executingScripts = new TreeSet<>();
  private User user = null;

  private static final String USER_PROMPT_SUFFIX = "> ";
  private static final String SCRIPT_PROMPT_SUFFIX = "$ ";

  private LineReader lineReader;
  @Setter
  private BufferedReader scriptReader = null;
  private final UserReader userReader;

  private enum InputState {USER, SCRIPT}

  @Setter
  private InputState inputState = InputState.USER;

  private boolean isConsoleRunning = false;
  private boolean exitFlag = false;

  public UserConsole(PrintStream out, CommandManager commandManager, Exchanger<Flat> exchanger, CollectionFilter collectionFilter) {
    this.out = out;
    this.commandManager = commandManager;
    this.exchanger = exchanger;
    this.collectionFilter = collectionFilter;
    this.builderWrapper = new ModelDtoBuilderWrapper(new ModelDtoBuilder());
    this.dtoReader = new DtoReader(this);
    this.userReader = new UserReader(this);
    initTerminal();
  }

  private void initTerminal() {
    try {
      Terminal terminal = TerminalBuilder.terminal();
      this.lineReader = LineReaderBuilder.builder().terminal(terminal).build();
    } catch (IOException e) {
      throw new RuntimeException("Failed to initialize console", e);
    }
  }

  private static String getWorkingDir() {
    try {
      String fullPath = System.getProperty("user.dir");
      return fullPath.substring(fullPath.lastIndexOf(File.separator) + 1);
    } catch (SecurityException e) {
      return "unknown";
    }
  }

  private static String colorText(String text, Colors color) {
    return color.text() + text + Colors.RESET.text();
  }

  private String getUserName() {
    return user != null ? user.getLogin() : "nullUser";
  }

  private String getPrompt() {
    String workingDir = getWorkingDir();
    String userName = getUserName();
    return colorText(userName, Colors.GREEN) + "@" + colorText(workingDir, Colors.GREEN) + colorText(USER_PROMPT_SUFFIX, Colors.CYAN);
  }

  private void printErr(String message) {
    out.println(colorText(message, Colors.RED));
  }

  /**
   * Starts interactive command handling loop
   */
  @Override
  public void run() {
    if (isConsoleRunning) {
      out.println("Console is already running");
      return;
    }
    isConsoleRunning = true;
    try {
      commandLoop();
    } catch (IOException e) {
      out.println("User console exited with IO exception: " + e.getMessage());
    } catch (EndOfFileException e) {
      out.println("Pressed Ctrl+D");
    } catch (ReconnectionTimeoutException e) {
      out.println(e.getMessage());
    }
    out.println("Console closed");
    isConsoleRunning = false;
  }

  @Override
  public void exit() {
    exitFlag = true;
  }

  /**
   * Changes console input mode to readLine from specified file.
   *
   * @param scriptName name of a file to readLine commands from.
   * @throws ScriptExecutionException if recursion of file not found.
   */
  @Override
  public void executeScript(String scriptName) throws ScriptExecutionException {
    if (executingScripts.contains(scriptName)) {
      throw new ScriptExecutionException("Recursion detected", scriptName);
    }
    try {
      runScript(scriptName);
    } catch (FileNotFoundException e) {
      throw new ScriptExecutionException("File not found", scriptName);
    }
  }

  @Override
  public String readLine() throws IOException {
    return readCommand(false);
  }

  @Override
  public byte[] readPassword() {
    return lineReader.readLine('*').getBytes();
  }

  @Override
  public void print(String text) {
    out.print(text);
  }

  private void commandLoop() throws IOException {
    String command;
    final CommandLineParser inputParser = new CommandLineParser();
    while (true) {
      if (exitFlag) return;

      command = readCommand(true);
      if (command == null) return;
      if (command.trim().equals("")) continue;

      ParsedInput inputData;
      try {
        inputData = inputParser.parse(command);
      } catch (IllegalArgumentException e) {
        printErr(e.getMessage());
        continue;
      }

      CommandInfo commandInfo = inputData.commandInfo;

      ModelDto data = null;
      switch (commandInfo.getType()) {
        case REQUIRES_DTO -> data = dtoReader.read();
        case AUTHENTICATION -> user = userReader.read();
      }

      updateCollection();

      String commandName = commandInfo.getName();
      String inlineArg = inputData.inlineArg;
      ExecutionPayload payload = ExecutionPayload.of(commandName, inlineArg, data, user);
      CommandExecutor commandExecutor = new CommandExecutor(commandInfo, payload);
      commandExecutor.executeCommand();
    }
  }

  private class CommandExecutor {
    private final CommandInfo commandInfo;
    private final ExecutionPayload payload;
    private final String commandName;

    CommandExecutor(CommandInfo commandInfo, ExecutionPayload payload) {
      this.commandInfo = commandInfo;
      this.payload = payload;
      this.commandName = commandInfo.getName();
    }

    void executeCommand() throws IOException {
      ExecutionMode mode = commandInfo.getExecutionMode();
      switch (mode) {
        case SERVER -> executeOnServer();
        case CLIENT -> executeLocally();
      }
    }

    private void executeLocally() {
      if (!commandManager.isRegistered(commandName)) {
        throw new IllegalArgumentException("Command can't be executed locally");
      }
      ExecutionResult result = commandManager.executeCommand(payload);
      handleExecutionResult(result);
    }

    private void executeOnServer() throws IOException {
      out.println("Sending command to server");
      exchanger.requestCommandExecution(payload);
      handleNewResponses();
    }
  }

  private void handleExecutionResult(ExecutionResult result) {
    if (!result.isSuccess()) {
      printErr(result.getMessage());
      return;
    }
    out.println(result.getMessage());
  }

  private void updateCollection() throws IOException {
    exchanger.requestCollectionChanges(collectionFilter.getCollectionVersion());
    try {
      CollectionChangelist<Flat> changelist = exchanger.receiveCollectionChanges();
      collectionFilter.applyChangelist(changelist);
    } catch (InvalidCredentialsException e) {
      printInvalidCredentials();
    }
  }

  private void handleNewResponses() throws IOException {
    try {
      ExecutionResult result = exchanger.receiveExecutionResult();
      handleExecutionResult(result);
    } catch (InvalidCredentialsException e) {
      printInvalidCredentials();
    }
  }

  private void printInvalidCredentials() {
    printErr("Invalid user credentials. Please register or login.");
  }

  private String readCommand(boolean prompt) throws IOException {
    switch (inputState) {
      case USER:
        return readUserCommand(prompt);
      case SCRIPT:
        if (prompt) out.println(scriptPrompt);
        String command = readCommandFromScript();
        if (command != null) {
          out.println(command);
          return command;
        }
        closeScript();
        return "";
    }
    return readUserCommand(prompt);
  }

  private String readUserCommand(boolean prompt) {
    if (prompt) return lineReader.readLine(getPrompt());
    return lineReader.readLine();
  }

  private String readCommandFromScript() throws IOException {
    return readLine(scriptReader, 99);
  }

  private String readLine(BufferedReader reader, int len) throws IOException {
    StringBuilder readResult = new StringBuilder();
    int currentPos = 0;
    int currentChar = reader.read();
    if (currentChar == -1) return null;

    while (currentPos++ < len) {
      readResult.append((char) currentChar);
      currentChar = reader.read();
      if (currentChar == -1 || (char) currentChar == '\n') break;
    }
    return readResult.toString();
  }

  private void runScript(String filename) throws FileNotFoundException {
    scriptReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
    scriptPrompt = colorText(getUserName(), Colors.YELLOW) + "#" + colorText(filename, Colors.YELLOW) + SCRIPT_PROMPT_SUFFIX;
    inputState = InputState.SCRIPT;
    out.println(colorText("--- Script " + filename + " started ---", Colors.BLUE));
    executingScripts.add(filename);
  }

  private void closeScript() throws IOException {
    if (scriptReader != null) {
      scriptReader.close();
      scriptReader = null;
    }
    inputState = InputState.USER;
    out.println();
    executingScripts.pollLast();
  }

  private record ParsedInput(CommandInfo commandInfo, String inlineArg) {}

  private class CommandLineParser {
    public ParsedInput parse(String userInput) throws IllegalArgumentException {
      String[] commandWithArgs;
      CommandInfo commandInfo;

      if (userInput.trim().equals("")) throw new IllegalArgumentException("Empty input");

      commandWithArgs = splitLine(userInput);
      String commandName = commandWithArgs[0];
      String inlineArg = commandWithArgs.length == 2 ? commandWithArgs[1] : "";

      commandInfo = fetchCommandInfo(commandName).orElseThrow(() -> new IllegalArgumentException(String.format("Command '%s' not avalible", commandName)));

      int providedArgsCount = commandWithArgs.length - 1;
      int requiresArgsCount = commandInfo.getArgsCount();
      if (requiresArgsCount != providedArgsCount) {
        throw new IllegalArgumentException(String.format("Command '%s' takes %d arguments, but '%d' were provided.%n", commandName, requiresArgsCount, providedArgsCount));
      }

      // if element input needed: call FieldsReader.
      if (commandInfo.getType() == CommandType.REQUIRES_DTO) {
        if (commandInfo.getArgsCount() == 1 && !inlineArg.matches("\\d+")) {
          throw new IllegalArgumentException("Command argument should be an integer");
        }
      }
      return new ParsedInput(commandInfo, inlineArg);
    }

    private String[] splitLine(String userInput) {
      return userInput.trim().split("\\s+", 0);
    }

    private Optional<CommandInfo> fetchCommandInfo(String commandName) {
      CommandNameToInfo nameToInfo = commandManager.getUseraccessibleCommandsInfo();
      if (!nameToInfo.containsKey(commandName)) {
        return Optional.empty();
      }
      return Optional.of(nameToInfo.get(commandName));
    }
  }
}