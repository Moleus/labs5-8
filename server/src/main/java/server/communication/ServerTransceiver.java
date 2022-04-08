package server.communication;

import communication.AbstractTransiever;
import communication.Transceiver;
import communication.packaging.Message;
import communication.packaging.Request;
import exceptions.RecievedInvalidObjectException;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Optional;

@Log4j2
public class ServerTransceiver extends AbstractTransiever {
  private ServerTransceiver(SocketChannel socketChannel) {
    super(socketChannel);
  }

  public static Transceiver of(SocketChannel socketChannel) {
    return new ServerTransceiver(socketChannel);
  }

  @Override
  public Optional<Request> recieve() throws IOException {
    return super.readObject().map(this::checkRequestInstance);
  }

  protected Request checkRequestInstance(Message messageObj) {
    if (!(messageObj instanceof Request request)) {
      throw new RecievedInvalidObjectException(Request.class, messageObj.getClass());
    }
    return request;
  }
}
