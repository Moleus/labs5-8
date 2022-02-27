package communication;

import communication.packaging.Message;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface Transceiver {
   void send(Message message) throws IOException;
   Message recieve() throws IOException, ClassNotFoundException;
   void newSocketChannel(SocketChannel socketChannel);
}
