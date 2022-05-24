package server.handlers;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public interface ChannelHandler {
  void handleChannelRead(ChannelWrapper channel, ByteBuffer byteBuffer, SelectionKey key);
}
