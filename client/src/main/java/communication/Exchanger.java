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

public interface Exchanger {
  void requestFullCollection();

  void requestCollectionChanges(Long currentVersion);

  void requestCommandExecution(ExecutionPayload payload);

  void requestAccessibleCommandsInfo();

  <T extends Model> CollectionChangelist<T> receiveCollectionChanges() throws IOException, InvalidCredentialsException;

  void requestLogin(User user);

  void requestRegister(User user);

  <T extends Model> CollectionWrapper<T> receiveFullCollection() throws IOException, InvalidCredentialsException;

  ExecutionResult receiveExecutionResult() throws IOException, InvalidCredentialsException;

  CommandNameToInfo receiveAccessibleCommandsInfo() throws IOException, InvalidCredentialsException;

  void validateLogin() throws InvalidCredentialsException, IOException;

  void validateRegister() throws InvalidCredentialsException, IOException;
}