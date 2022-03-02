package communication;

import communication.packaging.Message;
import exceptions.ConnectionIsDownException;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface Transceiver {
   void send(Message message) throws IOException;

   Message recieve() throws IOException, ClassNotFoundException, ConnectionIsDownException;

   void newSocketChannel(SocketChannel socketChannel);
}
