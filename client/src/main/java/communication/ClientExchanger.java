package communication;

import collection.CollectionChangelist;
import collection.CollectionWrapper;
import commands.CommandNameToInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import communication.packaging.BaseRequest;
import communication.packaging.Request;
import communication.packaging.Response;
import exceptions.InvalidCredentialsException;
import exceptions.ReceivedInvalidObjectException;
import exceptions.ReconnectionTimeoutException;
import model.data.Model;
import user.User;

import java.io.IOException;
import java.util.Optional;

public class ClientExchanger implements Exchanger {
  private final Transceiver transceiver;
  private final Session clientSession;

  private User user;
  private RequestPurpose lastRequestPurpose;

  public ClientExchanger(Transceiver transceiver, Session clientSession) {
    this.transceiver = transceiver;
    this.clientSession = clientSession;
  }

  @Override
  public void requestAccessibleCommandsInfo() {
    makeNewRequest(RequestPurpose.GET_COMMANDS, null);
  }

  @Override
  public void requestFullCollection() {
    makeNewRequest(RequestPurpose.INIT_COLLECTION, null);
  }

  @Override
  public void requestCollectionChanges(Long currentVersion) {
    makeNewRequest(RequestPurpose.GET_CHANGELIST, currentVersion);
  }

  @Override
  public void requestCommandExecution(ExecutionPayload payload) {
    makeNewRequest(RequestPurpose.CHANGE_COLLECTION, payload);
  }

  @Override
  public void requestLogin(User user) {
    this.user = user;
    makeNewRequest(RequestPurpose.LOGIN, null);
  }


  @Override
  public void requestRegister(User user) {
    this.user = user;
    makeNewRequest(RequestPurpose.REGISTER, null);
  }

  private void makeNewRequest(RequestPurpose purpose, Object payload) {
    lastRequestPurpose = purpose;
    Request request = BaseRequest.of(purpose, payload, user);
    sendWithReconnect(request);
  }

  private void sendWithReconnect(Request request) {
    try {
      transceiver.send(request);
      return;
    } catch (IOException e) {
      reconnectOrThrowTimeout();
    }
    sendWithReconnect(request);
  }

  private void reconnectOrThrowTimeout() {
    int RECONNECTION_SECONDS = 15;
    if (!clientSession.reconnect(RECONNECTION_SECONDS)) {
      System.out.println("Failed to connect to server");
      throw new ReconnectionTimeoutException();
    }
    transceiver.newSocketChannel(clientSession.getSocketChannel());
  }

  @Override
  public <T extends Model> CollectionWrapper<T> receiveFullCollection() throws InvalidCredentialsException, IOException {
    checkPurposeMatch(RequestPurpose.INIT_COLLECTION);

    Response response = receiveAndCheckResponse();
    Object payload = readPayload(response);

    return castObjTo(payload, CollectionWrapper.class);
  }

  @Override
  public <T extends Model> CollectionChangelist<T> receiveCollectionChanges() throws InvalidCredentialsException, IOException {
    checkPurposeMatch(RequestPurpose.GET_CHANGELIST);

    Response response = receiveAndCheckResponse();
    Object payload = readPayload(response);

    return castObjTo(payload, CollectionChangelist.class);
  }

  @Override
  public CommandNameToInfo receiveAccessibleCommandsInfo() throws InvalidCredentialsException, IOException {
    checkPurposeMatch(RequestPurpose.GET_COMMANDS);

    Response response = receiveAndCheckResponse();
    Object payload = readPayload(response);

    return castObjTo(payload, CommandNameToInfo.class);
  }

  @Override
  public ExecutionResult receiveExecutionResult() throws InvalidCredentialsException, IOException {
    checkPurposeMatch(RequestPurpose.CHANGE_COLLECTION);

    Response response = receiveAndCheckResponse();
    Object payload = readPayload(response);

    return castObjTo(payload, ExecutionResult.class);
  }

  @Override
  public void validateLogin() throws InvalidCredentialsException, IOException {
    checkPurposeMatch(RequestPurpose.LOGIN);
    receiveAndCheckResponse();
  }

  @Override
  public void validateRegister() throws InvalidCredentialsException, IOException {
    checkPurposeMatch(RequestPurpose.REGISTER);
    receiveAndCheckResponse();
  }

  @SuppressWarnings("unchecked")
  private <U> U castObjTo(Object obj, Class<U> toClass) {
    if (!(toClass.isInstance(obj))) {
      throw new ReceivedInvalidObjectException(toClass, obj.getClass());
    }
    return (U) obj;
  }

  private void checkPurposeMatch(RequestPurpose expected) {
    if (!lastRequestPurpose.equals(expected)) {
      throw new IllegalCallerException("Previous request purpose should be: " + expected);
    }
  }

  private Response receiveAndCheckResponse() throws InvalidCredentialsException, IOException {
    Response response = receiveWithReconnect().orElseThrow(() -> new ReceivedInvalidObjectException(Response.class, "Empty response"));
    checkResponseStatus(response);
    return response;
  }

  private Optional<Response> receiveWithReconnect() throws IOException {
    try {
      return transceiver.recieve().map(Response.class::cast);
    } catch (IOException e) {
      // failed to read. Usually, because of server shutdown.
      reconnectOrThrowTimeout();
    }
    throw new IOException("Server was unavailable. Please, repeat the request");
  }

  private void checkResponseStatus(Response response) throws InvalidCredentialsException {
    switch (response.getResponseCode()) {
      case INVALID_PAYLOAD -> throw new IllegalArgumentException("Invalid payload provided to server");
      case AUTH_FAILED -> throw new InvalidCredentialsException();
    }
  }

  private Object readPayload(Response response) {
    return response.getPayload().orElseThrow(() -> new ReceivedInvalidObjectException(Object.class, "empty payload"));
  }
}