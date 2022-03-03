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
  void requestFullCollection() throws ReconnectionTimoutException;

  void requestCollectionChanges(Long currentVersion) throws ReconnectionTimoutException;

  void requestCommandExecution(ExecutionPayload payload) throws ReconnectionTimoutException;

  void requestAccessibleCommandsInfo() throws ReconnectionTimoutException;

  CollectionChangelist recieveCollectionChanges() throws ReconnectionTimoutException, ResponseCodeException, IOException;

  CollectionWrapper recieveFullColection() throws ReconnectionTimoutException, ResponseCodeException, IOException;

  ExecutionResult recieveExecutionResult() throws ReconnectionTimoutException, ResponseCodeException, IOException;

  CommandNameToInfo recieveAccessibleCommandsInfo() throws ReconnectionTimoutException, ResponseCodeException, IOException;
}
