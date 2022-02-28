package server.communication;

import communication.AbstractTransiever;
import communication.Transceiver;
import communication.packaging.Message;
import communication.packaging.Request;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.channels.SocketChannel;

@Log4j2
public class ServerTransceiver extends AbstractTransiever {
  private ServerTransceiver(SocketChannel socketChannel) {
    super(socketChannel);
  }

  public static Transceiver of(SocketChannel socketChannel) {
    return new ServerTransceiver(socketChannel);
  }

  @Override
  public Request recieve() throws IOException, ClassNotFoundException {
    Message messageObj = readObject();
    log.info("Recieved new object from {}", socketChannel.getRemoteAddress());
    if (!(messageObj instanceof Request requestObj)) {
      throw new InvalidObjectException("Recieved object is not an instance of Request");
    }
    return requestObj;
  }
}
