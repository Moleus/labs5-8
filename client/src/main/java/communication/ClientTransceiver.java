package communication;

import communication.packaging.Message;
import communication.packaging.Response;
import exceptions.ReceivedInvalidObjectException;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Optional;

public class ClientTransceiver extends AbstractTransiever {
  public ClientTransceiver(SocketChannel socketChannel) {
    super(socketChannel);
  }

  @Override
  public Optional<Message> recieve() throws IOException {
    return super.readObject().map(this::checkResponseInstance);
  }

  protected Response checkResponseInstance(Message messageObj) {
    if (!(messageObj instanceof Response response)) {
      throw new ReceivedInvalidObjectException(Response.class, messageObj.getClass());
    }
    return response;
  }
}
