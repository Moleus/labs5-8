package utils;

import exceptions.ScriptExecutionException;

public interface Console extends Exitable, ScriptExecutor {
  void run();
  void executeScript(String scriptName) throws ScriptExecutionException;
}
