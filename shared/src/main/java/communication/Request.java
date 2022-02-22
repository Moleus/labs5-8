package communication;

import commands.ExecutionPayload;

public interface Request {
  String getCommandName();
  ExecutionPayload getExecutionPayload();
}
