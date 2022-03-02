package communication;

import commands.CommandNameToInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;

import java.io.IOException;
import java.util.Optional;

public interface Exchanger {
  void requestCollectionUpdate() throws IOException;

  ExecutionResult readExecutionResponse() throws IOException, ClassNotFoundException;

  void requestAccessibleCommandsInfo() throws IOException;

  Optional<CommandNameToInfo> readaccessibleCommandsInfoResponse();
}
