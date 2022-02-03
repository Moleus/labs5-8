package app.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.collection.FieldsInputMode;
import app.collection.FieldsReader;
import app.collection.FlatBuilder;
import app.collection.data.Flat;
import app.commands.*;
import app.common.CommandRequest;
import app.common.Request;
import app.common.Response;
import app.exceptions.*;

public class Console {
  private final Map<String, Command> userCommands;
  private final CommandManager commandManager;
  private final FieldsReader fieldsReader;
  private final List<String> executedScripts = new ArrayList<>();

  // In lab 5 read from stdin.
  // In lab 6 will be moved to client side.
  // Read input
  // launch commands ( commandHandler(String strCommand)
  // handle exit codes
  // interactive mode / script mode
  public Console(CommandManager commandManager, Map<String, Command> userCommands) {
    this.userCommands = userCommands;
    this.commandManager = commandManager;
    this.fieldsReader = new FieldsReader(Flat.class);
  }

  // должна принимать inputStream, outputStream и работать с чтением из скрипта, как с пользователем
  public void run(BufferedReader defaultReader) throws InterruptedException, IOException {
    commandLoop(defaultReader);
  }

  private void commandLoop(BufferedReader bufferedReader) throws IOException, InterruptedException {
    while (true) {
      if (!bufferedReader.ready()) Thread.sleep(100);
      try {
        String userInput = bufferedReader.readLine();

        if (userInput.trim().equals("")) {
          continue;
        }

        // parse user input and fetch info about command
        String[] commandWithArg = parseUserInput(userInput);
        String commandName = commandWithArg[0];
        String inlineArg = commandWithArg.length == 2 ? commandWithArg[1] : "";
        CommandInfo commandInfo = fetchCommandInfo(commandName);

        // check if count arguments matches input
        int requiresArgsCount = commandInfo.getArgsCount();
        int providedArgsCount = commandWithArg.length - 1;
        if (requiresArgsCount != providedArgsCount) {
          System.out.printf("This command takes %d arguments, but you provided %d.\n", requiresArgsCount, providedArgsCount);
          continue;
        }

        if (commandName.equals("execute_script")) {
          if (executedScripts.contains(inlineArg)) {
            System.out.printf("Script recursion detected! Script '%s' is already executing", inlineArg);
            continue;
          }
          executedScripts.add(inlineArg);
          executeScript(inlineArg);
          executedScripts.remove(executedScripts.size() - 1);
          continue;
        }
        // if element input needed: call FieldsReader.
        Object newFlat = constructObjectFromInput(bufferedReader, commandInfo);
        // Pack request object and send to "bridge" object.
        Request request = createRequest(commandName, inlineArg, newFlat);
        Response response = commandManager.executeCommand(request);
        handleResponse(response);
      } catch (ReadFailedException | CLIException | CommandNotRegisteredException e) {
        e.getMessage();
      } catch (IOException e) {
        e.printStackTrace();
        System.out.println("You pressed Ctrl+d");
        // TODO: handle null from script
        break;
      }
    }
    // remove script name from list (maybe just remove last element)
  }

  private void executeScript(String filePath) {
    try {
      BufferedReader reader = getFileReader(filePath);
      commandLoop(reader);
    } catch (FileNotFoundException e) {
      System.out.printf("Can't execute script '%s'. File not found.\n", filePath);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private BufferedReader getFileReader(String filePath) throws FileNotFoundException {
    return new BufferedReader(new FileReader(filePath));
  }

  // split string into command and arguments.
  private String[] parseUserInput(String userInput) throws ReadFailedException, CLIException {
    if (userInput == null) {
      throw new ReadFailedException("Read stream is closed.");
    }
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
      throw new CommandNotRegisteredException("Unknown command name " + commandName);
    }
    return userCommands.get(commandName).getInfo();
  }

  /**
   * Returns null if additional input for object creation is not needed.
   * @return Created Flat object
   */
  private Object constructObjectFromInput(BufferedReader reader, CommandInfo commandInfo) throws ReadFailedException {
    Object newFlat = null;
    if (commandInfo.isHasComplexArgs()) {
      Object[] additionalArgs = fieldsReader.read(reader, FieldsInputMode.INTERACTIVE);
      newFlat = FlatBuilder.getInstance().buildAccessible(additionalArgs);
    }
    return newFlat;
  }

  private Request createRequest(String commandName, String inlineArg, Object dataObject) {
    return CommandRequest.valueOf(commandName, ExecutionPayload.valueOf(inlineArg, dataObject));
  }

  private void handleResponse(Response response) {
    ExecutionResult result = response.getExecutionResult();
    if (!result.isSuccess()) {
      System.out.println("Error occurred on command execution: ");
    } else {
      System.out.println("Execution completed successfully: ");
    }
    System.out.println(result.getMessage());
  }
}
