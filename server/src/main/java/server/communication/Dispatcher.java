package server.communication;

import server.handlers.ChannelWrapper;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public interface Dispatcher {
  void onChannelReadEvent(ChannelWrapper channel, ByteBuffer byteBuffer, SelectionKey key);

  void stop();
}