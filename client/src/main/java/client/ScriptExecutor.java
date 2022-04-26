package client;

import exceptions.ScriptExecutionException;

public interface ScriptExecutor {
  void executeScript(String filename) throws ScriptExecutionException;
}
