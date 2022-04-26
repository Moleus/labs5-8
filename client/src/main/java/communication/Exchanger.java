package communication;

import collection.CollectionChangelist;
import collection.CollectionWrapper;
import commands.CommandNameToInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import exceptions.InvalidCredentialsException;
import model.data.Model;
import user.User;

import java.io.IOException;

public interface Exchanger<T extends Model> {
  void requestFullCollection();

  void requestCollectionChanges(Long currentVersion);

  void requestCommandExecution(ExecutionPayload payload);

  void requestAccessibleCommandsInfo();

  CollectionChangelist<T> receiveCollectionChanges() throws IOException, InvalidCredentialsException;

  void requestLogin(User user);

  void requestRegister(User user);

  CollectionWrapper<T> receiveFullCollection() throws IOException, InvalidCredentialsException;

  ExecutionResult receiveExecutionResult() throws IOException, InvalidCredentialsException;

  CommandNameToInfo receiveAccessibleCommandsInfo() throws IOException, InvalidCredentialsException;

  void validateLogin() throws InvalidCredentialsException, IOException;

  void validateRegister() throws InvalidCredentialsException, IOException;
}