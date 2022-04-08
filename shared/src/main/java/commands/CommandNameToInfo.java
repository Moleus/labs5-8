package commands;

import java.util.HashMap;
import java.util.Map;

public class CommandNameToInfo extends HashMap<String, CommandInfo> {
  private CommandNameToInfo(Map<String, CommandInfo> commandNameToInfo) {
    super(commandNameToInfo);
  }

  public static CommandNameToInfo of(Map<String, CommandInfo> commandNameToInfo) {
    return new CommandNameToInfo(commandNameToInfo);
  }
}
