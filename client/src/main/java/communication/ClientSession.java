package communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class ClientSession implements Session {
  private final InetSocketAddress remoteAddress;
  private SocketChannel socketChannel;

  public ClientSession(String host, int port) {
    remoteAddress = new InetSocketAddress(host, port);
  }

  @Override
  public boolean connect() {
    try {
      socketChannel = SocketChannel.open(remoteAddress);
    } catch (IOException e) {
      return false;
    }
    return isConnected();
  }

  @Override
  public boolean reconnect() {
    int connectionTriesCounter = 0;
    try {
      disconnect();
    } catch (IOException ignore) {}

    while(connectionTriesCounter++ < 15) {
      try {
        if (!connect()) {
          Thread.sleep(1000);
          System.out.println("Reconnecting to server");
        } else {
          return true;
        }
        } catch (InterruptedException ignored) {}
    }
    return false;
  }

  @Override
  public void disconnect() throws IOException {
    if (socketChannel != null) {
      socketChannel.close();
    }
  }

  @Override
  public boolean isConnected() {
    return socketChannel.isConnected();
  }

  @Override
  public SocketChannel getSocketChannel() {
    return socketChannel;
  }
}
