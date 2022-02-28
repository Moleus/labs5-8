package utils;

import commands.Command;
import exceptions.ScriptExecutionException;

import java.util.Map;

public interface Console extends Exitable, ScriptExecutor {
  void run();
  void executeScript(String scriptName) throws ScriptExecutionException;
  void registerLocalCommnands(Map<String, Command> commands);
}
