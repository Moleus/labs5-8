package client;

import collection.CollectionChangelist;
import collection.CollectionFilter;
import commands.*;
import communication.Exchanger;
import exceptions.*;
import lombok.Setter;
import model.ModelDto;
import model.builder.BuilderWrapper;
import model.builder.ModelDtoBuilderWrapper;
import model.data.ModelDtoBuilder;
import utils.Console;

import java.io.*;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.TreeSet;

/**
 * This class creates interactive prompt, reads user input, checks commands and sends request to manager.
 */
public class UserConsole implements Console {
  private final BufferedReader in;
  private final PrintStream out;
  private final CommandManager commandManager;
  private final Exchanger exchanger;
  private final CollectionFilter collectionFilter;
  private final BuilderWrapper<ModelDto> builderWrapper;

  // contains username + working dir + >
  private final String userPrompt;
  // contains username + script name + $
  private String scriptPrompt;


  private final NavigableSet<String> executingScripts = new TreeSet<>();
  private final String userName = "dev";

  private static final String USER_PROMPT_SUFFIX = "> ";
  private static final String SCRIPT_PROMPT_SUFFIX = "$ ";


  @Setter
  private BufferedReader scriptReader = null;

  private enum InputState {USER, SCRIPT}

  @Setter
  private InputState inputState = InputState.USER;

  private boolean isConsoleRunning = false;
  private boolean exitFlag = false;

  public UserConsole(BufferedReader in, PrintStream out, CommandManager commandManager, Exchanger exchanger, CollectionFilter collectionFilter) {
    this.in = in;
    this.out = out;
    this.commandManager = commandManager;
    this.exchanger = exchanger;
    this.collectionFilter = collectionFilter;
    this.builderWrapper = new ModelDtoBuilderWrapper(new ModelDtoBuilder());
    this.userPrompt = createUserPrompt(userName, getWorkingDir());
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

  private static String createUserPrompt(String userName, String dir) {
    return colorText(userName, Colors.GREEN) + "@" + colorText(dir, Colors.GREEN) + colorText(USER_PROMPT_SUFFIX, Colors.CYAN);
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
      out.println("User console exited with IO exception");
    }
    out.println("Console closed");
    isConsoleRunning = false;
  }

  @Override
  public void exit() {
    exitFlag = true;
  }

  /**
   * Changes console input mode to read from specified file.
   *
   * @param scriptName name of a file to read commands from.
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

  private void commandLoop() throws IOException {
    String command;
    final CommandLineParser inputParser = new CommandLineParser();
    while (true) {
      if (exitFlag) return;

      printPrompt();

      command = readCommand();
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
      boolean additionalInputNeeded = commandInfo.isHasComplexArgs();

      ModelDto dataValues = null;
      if (additionalInputNeeded) {
        dataValues = readNewModelDto();
      }

      updateCollection();

      String commandName = commandInfo.getName();
      String inlineArg = inputData.inlineArg;
      ExecutionPayload payload = ExecutionPayload.of(commandName, inlineArg, dataValues);
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

    void executeCommand() {
      ExecutionMode mode = commandInfo.getExecutionMode();
      switch (mode) {
        case SERVER -> executeOnServer();
        case CLIENT -> executeLocaly();
      }
    }

    private void executeLocaly() {
      if (!commandManager.isRegistered(commandName)) {
        throw new IllegalArgumentException("Command can't be executed localy");
      }
      ExecutionResult result = commandManager.executeCommand(payload);
      handleExecutionResult(result);
    }

    private void executeOnServer() {
      try {
        out.println("Sending command to server");
        exchanger.requestCommandExecution(payload);
        handleNewResponses();
      } catch (ReconnectionTimoutException e) {
        printErr("Failed to execute command on server.");
        exit();
      }
    }
  }

  private void handleExecutionResult(ExecutionResult result) {
    if (!result.isSuccess()) {
      printErr(result.getMessage());
      return;
    }
    out.println(result.getMessage());
  }

  private void updateCollection() {
    try {
      exchanger.requestCollectionChanges(collectionFilter.getCollectionVersion());
      CollectionChangelist changelist = exchanger.recieveCollectionChanges();
      collectionFilter.applyChangelist(changelist);
    } catch (ReconnectionTimoutException e) {
      printErr("Exiting.");
      exit();
    } catch (ResponseCodeException | IOException e) {
      printErr(e.getMessage());
    }
  }

  private void handleNewResponses() throws ReconnectionTimoutException {
    try {
      ExecutionResult result = exchanger.recieveExecutionResult();
      handleExecutionResult(result);
    } catch (ResponseCodeException | IOException e) {
      printErr(e.getMessage());
    }
  }

  private String readCommand() throws IOException {
    switch (inputState){
      case USER:
        return readUserCommand();
      case SCRIPT:
        String command = readCommandFromScript();
        if (command != null) {
          out.println(command);
          return command;
        }
        closeScript();
        return "";
    }
    return readUserCommand();
  }

  private String readUserCommand() throws IOException {
    return readLine(in, 100);
  }

  private void printPrompt() {
    switch (inputState) {
      case USER -> out.print(userPrompt);
      case SCRIPT -> out.println(scriptPrompt);
    }
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
    scriptPrompt = colorText(userName, Colors.YELLOW) + "#" + colorText(filename, Colors.YELLOW) + SCRIPT_PROMPT_SUFFIX;
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
      if (commandInfo.isHasComplexArgs()) {
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

  private ModelDto readNewModelDto() throws IOException {
    int fieldsCount = builderWrapper.getFieldsCount();
    builderWrapper.setPosition(0);

    while (builderWrapper.getPosition() < fieldsCount) {
      System.out.printf("Enter %s: ", builderWrapper.getDescription());
      String line = readCommand();
      try {
        builderWrapper.setValue(line);
      } catch (ValueFormatException | ValueConstraintsException e) {
        System.out.println(e.getMessage());
        continue;
      }
      builderWrapper.step();
    }

    return builderWrapper.build();
  }
}