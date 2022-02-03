package app.exceptions;

import app.commands.Command;

public class CommandNotRegisteredException extends Exception {
  public CommandNotRegisteredException(Command command) {
    super(String.format("Command %s not registered in command manager", command.getName()));
  }
  public CommandNotRegisteredException(String commandName) {
    super(String.format("Command %s not registered in command manager", commandName));
  }
}
