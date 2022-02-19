package app.utils;

import app.collection.FieldsInputMode;
import app.collection.FieldsReader;
import app.collection.data.Flat;
import app.commands.*;
import app.common.CommandRequest;
import app.common.Request;
import app.common.Response;
import app.exceptions.ReadFailedException;
import lombok.Data;

import java.io.*;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.TreeSet;

/**
 * This class creates interactive prompt, reads user input, checks commands and sends request to manager.
 */
public class Console {
  private final Map<String, Command> accessibleCommands;
  private final CommandManager commandManager;
  private final FieldsReader fieldsReader;
  private final NavigableSet<String> executingScripts = new TreeSet<>();
  private final String userName = "dev";

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
    this.accessibleCommands = userCommands;
    this.commandManager = commandManager;
    this.fieldsReader = new FieldsReader(Flat.class);
    this.userPrompt = createUserPrompt(userName, getWorkingDir());
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

  /**
   * Starts interactive command handling loop
   */
  public void run() throws IOException {
    commandLoop();
  }

  private void commandLoop() throws IOException {
    String command;
    final CommandLineParser inputParser = new CommandLineParser();
    while (true) {
      command = readCommand();
      if (command == null || command.trim().equals("exit")) return;
      if (command.trim().equals("")) continue;

      ParsedInput inputData;
      try {
        inputData = inputParser.parse(command);
      } catch (IllegalArgumentException e) {
        printErr(e.getMessage());
        continue;
      }

      String commandName = inputData.getCommandInfo().getName();
      String inlineArg = inputData.getInlineArg();
      boolean additionalInputNeeded = inputData.getCommandInfo().isHasComplexArgs();

      Object[] dataValues = new Object[0];
      if (additionalInputNeeded) {
        try {
          dataValues = readAdditionalInput();
        } catch (ReadFailedException e) {
          printErr(e.getMessage());
          continue;
        }
      }
      if (commandName.equals("execute_script")) {
        executeScriptCommand(inlineArg);
        continue;
      }

      // Pack request object and send to "bridge" object.
      Request request = createRequest(commandName, inlineArg, dataValues);
      Response response = commandManager.executeCommand(request);
      handleResponse(response);
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
        return readUserCommand();
    }
    return readUserCommand();
  }

  private String readUserCommand() throws IOException {
    out.print(userPrompt);
    return readLine(in, 100);
  }

  private String readCommandFromScript() throws IOException {
    out.print(scriptPrompt);
    return readLine(scriptReader, 99);
  }

  private void executeScriptCommand(String scriptName) {
      if (executingScripts.contains(scriptName)) {
        printErr(String.format("Script recursion detected! Script '%s' won't be executed%n", scriptName));
        return;
      }
      try {
        runScript(scriptName);
      } catch (FileNotFoundException e) {
        printErr(String.format("Can't execute script '%s'. File not found.%n", scriptName));
      }
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

  @Data(staticConstructor = "of")
  private static class ParsedInput {
    private final CommandInfo commandInfo;
    private final String inlineArg;
  }

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
      return ParsedInput.of(commandInfo, inlineArg);
    }

    private String[] splitLine(String userInput) {
      return userInput.trim().split("\\s+", 0);
    }

    private Optional<CommandInfo> fetchCommandInfo(String commandName) {
      if (!accessibleCommands.containsKey(commandName)) {
        return Optional.empty();
      }
      return Optional.of(accessibleCommands.get(commandName).getInfo());
    }
  }

  private Object[] readAdditionalInput() throws ReadFailedException {
    switch (inputState) {
      case USER:
        return fieldsReader.read(in, FieldsInputMode.INTERACTIVE);
      case SCRIPT:
        return fieldsReader.read(scriptReader, FieldsInputMode.SCRIPT);
    }
    throw new RuntimeException("Unknown console state");
  }

  public static Request createRequest(String commandName, String inlineArg, Object[] dataValues) {
    return CommandRequest.valueOf(commandName, ExecutionPayload.of(inlineArg, dataValues));
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