package app.exceptions;

public class CommandNotRegisteredException extends Exception {
  public CommandNotRegisteredException(String commandName) {
    super(String.format("Command '%s' not registered in command manager", commandName));
  }
}
