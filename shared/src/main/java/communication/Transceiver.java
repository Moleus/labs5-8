package communication;

import communication.packaging.Message;
import exceptions.RecievedInvalidObjectException;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Optional;

public interface Transceiver {
   void send(Message message) throws IOException;

   Optional<? extends Message> recieve() throws IOException, RecievedInvalidObjectException;

   void newSocketChannel(SocketChannel socketChannel);
}
