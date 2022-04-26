package server.handlers;

import java.nio.channels.SelectionKey;

public interface ChannelHandler {
  void handleChannelRead(ChannelWrapper channel, Object readObject, SelectionKey key);
}
