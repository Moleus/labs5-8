package communication;

import commands.CommandNameToInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import exceptions.ReconnectionTimoutException;

import java.io.IOException;
import java.util.Optional;

public interface Exchanger {
  void requestCollectionUpdate() throws IOException;

  void createCommandRequest(ExecutionPayload payload) throws IOException, ReconnectionTimoutException;

  ExecutionResult readExecutionResponse() throws IOException, ClassNotFoundException, ReconnectionTimoutException;

  void requestAccessibleCommandsInfo() throws IOException;

  Optional<CommandNameToInfo> readaccessibleCommandsInfoResponse() throws ReconnectionTimoutException;
}
