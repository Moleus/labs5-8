package communication;

import collection.CollectionChangelist;
import collection.CollectionWrapper;
import commands.CommandNameToInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import exceptions.ReconnectionTimoutException;
import exceptions.ResponseCodeException;

import java.io.IOException;

public interface Exchanger {
  void requestCollectionUpdate() throws ReconnectionTimoutException;

  void requestCommandExecution(ExecutionPayload payload) throws ReconnectionTimoutException;

  void requestAccessibleCommandsInfo() throws ReconnectionTimoutException;

  ExecutionResult recieveExecutionResult() throws ReconnectionTimoutException, ResponseCodeException;

  CollectionWrapper recieveCollectionWrapper() throws ReconnectionTimoutException, ResponseCodeException;

  CommandNameToInfo recieveAccessibleCommandsInfo() throws ReconnectionTimoutException, ResponseCodeException;
}
