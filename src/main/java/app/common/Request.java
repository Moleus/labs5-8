package app.common;

import app.commands.ExecutionPayload;

public interface Request {
  String getCommandName();
  ExecutionPayload getExecutionPayload();
}
