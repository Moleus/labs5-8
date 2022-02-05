package app.utils;

import app.collection.FieldsInputMode;
import app.collection.FieldsReader;
import app.collection.data.Flat;
import app.commands.*;
import app.common.CommandRequest;
import app.common.Request;
import app.common.Response;
import app.exceptions.CLIException;
import app.exceptions.CommandNotRegisteredException;
import app.exceptions.ReadFailedException;

import java.io.*;
import java.util.*;

public class Console {
  private final Map<String, Command> userCommands;
  private final CommandManager commandManager;
  private final FieldsReader fieldsReader;
  private final List<String> executingScripts = new ArrayList<>();
  private final String userName = "dev";

  private final List<String> commandsHistory = new ArrayList<>();

  private static final String USER_PROMPT_SUFFIX = "> ";
  private static final String SCRIPT_PROMPT_SUFFIX = "$ ";

  // contains username + working dir + >
  private final String userPrompt;
  // contains username + script name + $
  private String scriptPrompt;

  private final BufferedReader in;
  private final PrintStream out;

  private BufferedReader scriptReader = null;

  private enum InputState {USER, SCRIPT}
  private InputState inputState = InputState.USER;

  public Console(BufferedReader in, PrintStream out, CommandManager commandManager, Map<String, Command> userCommands) {
    this.in = in;
    this.out = out;
    this.userCommands = userCommands;
    this.commandManager = commandManager;
    this.fieldsReader = new FieldsReader(Flat.class);
    this.userPrompt = createUserPrompt(userName, getWorkingDir());
  }

  // должна принимать inputStream, outputStream и работать с чтением из скрипта, как с пользователем
  public void run() throws IOException {
    commandLoop();
  }

  private String invokeHistoryMode() throws IOException {
    ListIterator<String> commandInHistory = commandsHistory.listIterator(commandsHistory.size());
    if (!commandInHistory.hasPrevious()) {
      return null;
    }
    out.println("Enetered history mode. Press '<Return>' to choose a command. j/k - prev/next");
    int position = commandInHistory.previousIndex() + 1;
    String commandToInvoke = commandInHistory.previous();
    String keyPress;
    while (true) {
      out.printf("%d/%d: %s ", position, commandsHistory.size(), commandToInvoke);
      keyPress = Optional.ofNullable(in.readLine()).orElse("Null").trim();
      if (keyPress.equals(KeyBindings.HISTORY_UP.getBinding()) && commandInHistory.hasPrevious()) {
        commandToInvoke = commandInHistory.previous();
        position--;
      } else if (keyPress.equals(KeyBindings.HISTORY_DOWN.getBinding()) && commandInHistory.hasNext()) {
        commandToInvoke = commandInHistory.next();
        position++;
      } else if (keyPress.equals("")) {
        System.out.printf("Chose a command: '%s'%n", commandToInvoke);
        return commandToInvoke;
      } else {
        return null;
      }
    }
  }

  private void commandLoop() throws IOException {
    String command;
    while (true) {
      command = readCommand();
      if (command == null || command.trim().equals("exit")) return;
      if (command.trim().equals("")) continue;

      if (command.trim().equals("h")) {
        command = invokeHistoryMode();
        if (null == command) {
          continue;
        }
      }
      commandsHistory.add(command);

      String[] commandWithArg;
      try {
        commandWithArg = processLine(command);
      } catch (CLIException e) {
        e.getMessage();
        continue;
      }

      // parse user input and fetch info about command
      String commandName = commandWithArg[0];
      String inlineArg = commandWithArg.length == 2 ? commandWithArg[1] : "";
      CommandInfo commandInfo;
      try {
        commandInfo = fetchCommandInfo(commandName);
      } catch (CommandNotRegisteredException e) {
        printErr(e.getMessage());
        continue;
      }

      // check if count arguments matches input
      int requiresArgsCount = commandInfo.getArgsCount();
      int providedArgsCount = commandWithArg.length - 1;
      if (requiresArgsCount != providedArgsCount) {
        printErr(String.format("Command '%s' takes %d arguments, but '%d' were provided.%n", commandName, requiresArgsCount, providedArgsCount));
        continue;
      }

      if (commandName.equals("exit")) {
        break;
      }
      if (commandName.equals("execute_script")) {
        if (executingScripts.contains(inlineArg)) {
          printErr(String.format("Script recursion detected! Script '%s' won't be executed%n", inlineArg));
          continue;
        }
        try {
          runScript(inlineArg);
        } catch (FileNotFoundException e) {
          printErr(String.format("Can't execute script '%s'. File not found.%n", inlineArg));
        }
        continue;
      }

      // if element input needed: call FieldsReader.
      Object[] dataValues = null;
      try {
        if (commandInfo.isHasComplexArgs()) {
          dataValues = readAdditionalParameters();
        }
      } catch (ReadFailedException e) {
        printErr(e.getMessage());
        continue;
      }
      // Pack request object and send to "bridge" object.
      Request request = createRequest(commandName, inlineArg, dataValues);
      Response response = commandManager.executeCommand(request);
      handleResponse(response);
    }
  }

  private static String getWorkingDir() {
    try {
      String fullPath =  System.getProperty("user.dir");
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

  private static void printErr(String message) {
    System.err.println(colorText(message, Colors.RED));
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
        return readUserCommand();
    }
    return readUserCommand();
  }

  private String readUserCommand() throws IOException {
    out.print(userPrompt);
    return in.readLine();
  }

  private String readCommandFromScript() throws IOException {
    out.print(scriptPrompt);
    return scriptReader.readLine();
  }

  // split string into command and arguments.
  private String[] processLine(String userInput) throws CLIException {
    String[] commandWithArg = userInput.trim().split("\\s+", 0);
    if (commandWithArg.length == 0) {
      throw new CLIException("Please, enter a command");
    }
    if (commandWithArg.length > 2) {
      throw new CLIException("Too many arguments!");
    }
    // TODO: validate argument type
    return commandWithArg;
  }

  // check if command exists and return its info
  private CommandInfo fetchCommandInfo(String commandName) throws CommandNotRegisteredException {
    if (!userCommands.containsKey(commandName)) {
      throw new CommandNotRegisteredException(commandName);
    }
    return userCommands.get(commandName).getInfo();
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
    executingScripts.remove(executingScripts.size() - 1);
  }

  /**
   * Returns null if additional input for object creation is not needed.
   * @return Created Flat object
   */
  private Object[] readAdditionalParameters() throws ReadFailedException {
    if (scriptReader != null) {
      return fieldsReader.read(scriptReader, FieldsInputMode.SCRIPT);
    }
    return fieldsReader.read(in, FieldsInputMode.INTERACTIVE);
  }

  private Request createRequest(String commandName, String inlineArg, Object[] dataValues) {
    return CommandRequest.valueOf(commandName, ExecutionPayload.valueOf(inlineArg, dataValues));
  }

  private void handleResponse(Response response) {
    ExecutionResult result = response.getExecutionResult();
    if (!result.isSuccess()) {
      printErr(result.getMessage());
      return;
    }
    out.println(result.getMessage());
  }
}