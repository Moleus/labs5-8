package communication;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface Session {
  boolean connect();

  boolean reconnect(int countDownSecnods);

  void disconnect() throws IOException;

  boolean isConnected();

  SocketChannel getSocketChannel();
}
