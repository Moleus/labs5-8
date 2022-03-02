package communication;

import commands.CommandNameToInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import communication.packaging.*;
import exceptions.ConnectionIsDownException;
import exceptions.RecievedInvalidObjectException;
import exceptions.ReconnectionTimoutException;
import model.CollectionWrapper;

import java.io.IOException;
import java.util.Optional;

public class ClientExchanger implements Exchanger {
  private final Transceiver transceiver;
  private RequestPurpose lastRequestPurpose;
  private final Session clientSession;
  private final int RECONNECTION_SECONDS = 15;

  public ClientExchanger(Transceiver transceiver, Session clientSession) {
    this.transceiver = transceiver;
    this.clientSession = clientSession;
  }

  @Override
  public void requestCollectionUpdate() throws IOException {
    lastRequestPurpose = RequestPurpose.UPDATE_COLLECTION;
    Request request = BaseRequest.of(RequestPurpose.UPDATE_COLLECTION, null);
    transceiver.send(request);
  }

  @Override
  public void createCommandRequest(ExecutionPayload payload) throws IOException, ReconnectionTimoutException {
    lastRequestPurpose = RequestPurpose.EXECUTE;
    Request request = ExecutionRequest.of(payload);
    try {
      transceiver.send(request);
    } catch (IOException e) {
      if (clientSession.reconnect(RECONNECTION_SECONDS)) {
        transceiver.newSocketChannel(clientSession.getSocketChannel());
        System.out.println("Connection restored");
        transceiver.send(request);
        return;
      }
      System.out.println("Failed to connect to server");
      throw new ReconnectionTimoutException();
    }
  }

  @Override
  public ExecutionResult readExecutionResponse() throws IOException, ClassNotFoundException, ReconnectionTimoutException {
    if (!lastRequestPurpose.equals(RequestPurpose.EXECUTE)) {
      throw new IllegalCallerException("Last request purpose wasn't " + RequestPurpose.EXECUTE);
    }
    Response response;
    clientSession.getSocketChannel().configureBlocking(true);
    try {
      response = (Response) transceiver.recieve();
    } catch (ConnectionIsDownException e) {
      if (tryToReconnect()) {
        return readExecutionResponse();
      }
      System.err.println("Couldn't reconnect to server");
      throw new ReconnectionTimoutException();
    }
    if (!response.getResponseCode().equals(ResponseCode.SUCCESS)) {
      // TODO: Indicate to console that something in communication went wrong.
      throw new IOException("Recieved response with invalid status code");
    }
    return (ExecutionResult) response.getPayload().orElseThrow(RecievedInvalidObjectException::new);
  }

  public CollectionWrapper fetchUpdatedCollection(boolean block) throws IOException, ClassNotFoundException, ReconnectionTimoutException {
    Response response;
    clientSession.getSocketChannel().configureBlocking(block);
    try {
      response = (Response) transceiver.recieve();
    } catch (ConnectionIsDownException e) {
      if (tryToReconnect()) {
        return fetchUpdatedCollection(block);
      }
      System.err.println("Couldn't reconnect to server");
      throw new ReconnectionTimoutException();
    }
    if (!response.getResponseCode().equals(ResponseCode.SUCCESS)) {
      throw new IOException("Recieved response with invalid status code");
    }

    Object payload = response.getPayload().orElseThrow(RecievedInvalidObjectException::new);

    if (!(payload instanceof CollectionWrapper collectionWrapper)) {
      throw new RecievedInvalidObjectException("Can't parse Response object!");
    }
    return collectionWrapper;
  }

  @Override
  public void requestAccessibleCommandsInfo() throws IOException {
    lastRequestPurpose = RequestPurpose.GET_COMMANDS;
    Request request = new ClientCommandsRequest();
    System.out.println("New request sent");
    transceiver.send(request);
  }

  @Override
  public Optional<CommandNameToInfo> readaccessibleCommandsInfoResponse() throws ReconnectionTimoutException {
    Response response;
    Object payload;
    try {
      response = (Response) transceiver.recieve();
      // TODO: refactor me
      payload = response.getPayload().orElseThrow(() -> new ClassNotFoundException("Commands Map expected"));
    } catch (IOException | ClassNotFoundException e) {
      return Optional.empty();
    } catch (ConnectionIsDownException e) {
      if (!tryToReconnect()) {
        System.err.println("Couldn't reconnect to server");
        throw new ReconnectionTimoutException();
      }
      return readaccessibleCommandsInfoResponse();
    }

    if (!response.getResponseCode().equals(ResponseCode.SUCCESS)) {
      return Optional.empty();
    }
    if (!(payload instanceof CommandNameToInfo commandNameToInfo)) {
      return Optional.empty();
    }
    return Optional.of(commandNameToInfo);
  }

  public boolean tryToReconnect() {
    if (clientSession.reconnect(RECONNECTION_SECONDS)) {
      transceiver.newSocketChannel(clientSession.getSocketChannel());
      return true;
    }
    return false;
  }
}
