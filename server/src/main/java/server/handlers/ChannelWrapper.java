package server.handlers;

import server.communication.NioReactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

public interface ChannelWrapper {

  SelectableChannel getJavaChannel();

  ChannelHandler getHandler();

  void bind() throws IOException;

  void setReactor(NioReactor reactor);

  ByteBuffer read(SelectionKey key) throws IOException;

  void flush(SelectionKey key) throws IOException;

  void write(Object data, SelectionKey key);
}
