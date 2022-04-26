package utils;

import exceptions.ScriptExecutionException;
import interfaces.Exitable;

public interface Console extends Exitable, ScriptExecutor {
  void run();
  void executeScript(String scriptName) throws ScriptExecutionException;
}
