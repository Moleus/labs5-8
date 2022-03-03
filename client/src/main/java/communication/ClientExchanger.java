package communication;

import commands.CommandNameToInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import communication.packaging.BaseRequest;
import communication.packaging.Request;
import communication.packaging.Response;
import exceptions.RecievedInvalidObjectException;
import exceptions.ReconnectionTimoutException;
import exceptions.ResponseCodeException;

import java.io.IOException;
import java.util.Optional;

public class ClientExchanger implements Exchanger {
  private final Transceiver transceiver;
  private RequestPurpose lastRequestPurpose;
  private final Session clientSession;

  public ClientExchanger(Transceiver transceiver, Session clientSession) {
    this.transceiver = transceiver;
    this.clientSession = clientSession;
  }

  @Override
  public void requestAccessibleCommandsInfo() throws ReconnectionTimoutException {
    makeNewRequest(RequestPurpose.GET_COMMANDS, null);
  }


  @Override
  public void requestFullCollection() throws ReconnectionTimoutException {
    makeNewRequest(RequestPurpose.INIT_COLLECTION, null);
  }

  @Override
  public void requestCollectionChanges(Long currentVersion) throws ReconnectionTimoutException {
    makeNewRequest(RequestPurpose.UPDATE_COLLECTION, currentVersion);
  }

  @Override
  public void requestCommandExecution(ExecutionPayload payload) throws ReconnectionTimoutException {
    makeNewRequest(RequestPurpose.EXECUTE, payload);
  }

  private void makeNewRequest(RequestPurpose purpose, Object payload) throws ReconnectionTimoutException {
    lastRequestPurpose = purpose;
    Request request = BaseRequest.of(purpose, payload);
    sendWithReconnect(request);
  }

  private void sendWithReconnect(Request request) throws ReconnectionTimoutException {
    try {
      transceiver.send(request);
      return;
    } catch (IOException e) {
      reconnectOrThrowTimeout();
    }
    sendWithReconnect(request);
  }

  private void reconnectOrThrowTimeout() throws ReconnectionTimoutException {
    int RECONNECTION_SECONDS = 15;
    if (!clientSession.reconnect(RECONNECTION_SECONDS)) {
      System.out.println("Failed to connect to server");
      throw new ReconnectionTimoutException();
    }
    transceiver.newSocketChannel(clientSession.getSocketChannel());
    System.out.println("Connection restored. Sending request one more time.");
  }

  @Override
  public CollectionWrapper recieveCollectionWrapper() throws ReconnectionTimoutException, ResponseCodeException {
    checkPurposeMatch(RequestPurpose.UPDATE_COLLECTION);

    Response response = recieveAndCheckResponse();
    Object payload = readPayload(response);

    return castObjTo(payload, CollectionWrapper.class);
  }

  @Override
  public CommandNameToInfo recieveAccessibleCommandsInfo() throws ReconnectionTimoutException, ResponseCodeException, IOException {
    checkPurposeMatch(RequestPurpose.GET_COMMANDS);

    Response response = recieveAndCheckResponse();
    Object payload = readPayload(response);

    return castObjTo(payload, CommandNameToInfo.class);
  }

  @Override
  public ExecutionResult recieveExecutionResult() throws ReconnectionTimoutException, ResponseCodeException, IOException {
    checkPurposeMatch(RequestPurpose.EXECUTE);

    Response response = recieveAndCheckResponse();
    Object payload = readPayload(response);

    return castObjTo(payload, ExecutionResult.class);
  }

  @SuppressWarnings("unchecked")
  private <T> T castObjTo(Object obj, Class<T> toClass) {
    if (!(toClass.isInstance(obj))) {
      throw new RecievedInvalidObjectException(toClass, obj.getClass());
    }
    return (T) obj;
  }

  private void checkPurposeMatch(RequestPurpose expected) {
    if (!lastRequestPurpose.equals(expected)) {
      throw new IllegalCallerException("Previous request purpose should be: " + expected);
    }
  }

  private Response recieveAndCheckResponse() throws ResponseCodeException, ReconnectionTimoutException, IOException {
    Response response = recieveWithReconnect().orElseThrow(() -> new RecievedInvalidObjectException(Response.class, "Empty response"));
    checkResponseStatus(response);
    return response;
  }

  private Optional<Response> recieveWithReconnect() throws ReconnectionTimoutException, IOException {
    try {
      return transceiver.recieve().map(Response.class::cast);
    } catch (IOException e) {
      // failed to read. Usually, because of server shutdown.
      reconnectOrThrowTimeout();
    }
    throw new IOException("Server was unavailble. Please, repeat the request");
  }

  private void checkResponseStatus(Response response) throws ResponseCodeException {
    switch (response.getResponseCode()) {
      case INVALID_PAYLOAD -> throw new ResponseCodeException("Invalid payload provided");
      case UNAUTHORIZED -> throw new ResponseCodeException("Client is not authorized");
    }
  }

  private Object readPayload(Response response) {
    return response.getPayload().orElseThrow(() -> new RecievedInvalidObjectException(Object.class, "empty payload"));
  }
}