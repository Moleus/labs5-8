package utils;

import communication.Request;
import exceptions.ScriptExecutionException;

import java.io.IOException;

public interface Console {
  Request createRequest(String commandName, String inlineArg, Object[] dataValues);

  void run() throws IOException;

  void exit();

  void executeScript(String scriptName) throws ScriptExecutionException;
}
