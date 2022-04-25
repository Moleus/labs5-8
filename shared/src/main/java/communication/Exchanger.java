package communication;

import collection.CollectionChangelist;
import collection.CollectionWrapper;
import commands.CommandNameToInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import exceptions.ReconnectionTimoutException;
import exceptions.ResponseCodeException;
import model.data.Model;

import java.io.IOException;

public interface Exchanger<T extends Model> {
  void requestFullCollection() throws ReconnectionTimoutException;

  void requestCollectionChanges(Long currentVersion) throws ReconnectionTimoutException;

  void requestCommandExecution(ExecutionPayload payload) throws ReconnectionTimoutException;

  void requestAccessibleCommandsInfo() throws ReconnectionTimoutException;

  CollectionChangelist<T> receiveCollectionChanges() throws ReconnectionTimoutException, ResponseCodeException, IOException;

  CollectionWrapper<T> receiveFullCollection() throws ReconnectionTimoutException, ResponseCodeException, IOException;

  ExecutionResult receiveExecutionResult() throws ReconnectionTimoutException, ResponseCodeException, IOException;

  CommandNameToInfo receiveAccessibleCommandsInfo() throws ReconnectionTimoutException, ResponseCodeException, IOException;
}
