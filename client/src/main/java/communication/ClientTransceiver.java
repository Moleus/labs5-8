package communication;

import communication.packaging.Message;
import communication.packaging.Response;
import exceptions.RecievedInvalidObjectException;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class ClientTransceiver extends AbstractTransiever {
  public ClientTransceiver(SocketChannel socketChannel) {
    super(socketChannel);
  }

  @Override
  public Response recieve() throws IOException, ClassNotFoundException {
    Message messageObj = super.readObject();
    System.out.println("Recieving new object");
    if (!(messageObj instanceof Response responseObj)) {
      throw new RecievedInvalidObjectException("Can't parse Response object!");
    }
    return responseObj;
  }
}
