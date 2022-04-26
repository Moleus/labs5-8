package server.communication;

import server.handlers.ChannelWrapper;

import java.nio.channels.SelectionKey;

public interface Dispatcher {
  void onChannelReadEvent(ChannelWrapper channel, Object readObject, SelectionKey key);

  void stop();
}