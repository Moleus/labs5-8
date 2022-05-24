package server.handlers;

import communication.MessagingUtil;
import server.communication.NioReactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * It's an event Handler which reads and writes from a channel.
 */
public class ServerChannelWrapper implements ChannelWrapper {
  private final SelectableChannel channel;
  private final ChannelHandler handler;
  private final Map<SelectableChannel, Queue<Object>> channelToPendingWrites;
  private final int port;
  private NioReactor reactor;

  private final int DATA_LENGTH = 2048;
  private final Map<SocketChannel, ByteBuffer> channelToBuffer = new HashMap<>();

  public ServerChannelWrapper(ChannelHandler handler, int port) throws IOException {
    this.handler = handler;
    this.port = port;
    this.channel = ServerSocketChannel.open();
    this.channelToPendingWrites = new ConcurrentHashMap<>();
  }

  @Override
  public void bind() throws IOException {
    ServerSocketChannel javaChannel = getJavaChannel();
    javaChannel.socket().bind(new InetSocketAddress(port));
    javaChannel.configureBlocking(false);
  }

  /**
   * Injects the reactor in this channel.
   */
  @Override
  public void setReactor(NioReactor reactor) {
    this.reactor = reactor;
  }

  /**
   * Get channel.
   */
  @Override
  public ServerSocketChannel getJavaChannel() {
    return (ServerSocketChannel) channel;
  }

  @Override
  public ChannelHandler getHandler() {
    return handler;
  }

  /**
   * Returns data read from channel or null if buffer is not filled.
   */
  @Override
  public ByteBuffer read(SelectionKey key) throws IOException {
    SocketChannel socketChannel = (SocketChannel) key.channel();
    ByteBuffer byteBuffer = getBufferByChannel(socketChannel);
    int read = socketChannel.read(byteBuffer);

    if (byteBuffer.position() == DATA_LENGTH) {
      byteBuffer.position(0);
      return byteBuffer;
    }
    if (read == -1) {
      throw new IOException("Socket closed");
    }
    return null;
  }

  private ByteBuffer getBufferByChannel(SocketChannel channel) {
    if (!channelToBuffer.containsKey(channel)) {
      channelToBuffer.put(channel, ByteBuffer.allocate(DATA_LENGTH));
    }
    return channelToBuffer.get(channel);
  }

  /**
   * Writes all pending data to channel.
   */
  @Override
  public void flush(SelectionKey key) throws IOException {
    Queue<Object> pendingWrites = channelToPendingWrites.get(key.channel());
    Object pendingWrite;
    while ((pendingWrite = pendingWrites.poll()) != null) {
      doWrite(pendingWrite, key);
    }
    reactor.changeOps(key, SelectionKey.OP_READ);
  }

  /**
   * Add data to pending queue.
   */
  @Override
  public void write(Object data, SelectionKey key) {
    Queue<Object> pendingWrites = this.channelToPendingWrites.get(key.channel());
    if (pendingWrites == null) {
      pendingWrites = new ConcurrentLinkedQueue<>();
      this.channelToPendingWrites.put(key.channel(), pendingWrites);
    }
    pendingWrites.add(data);
    reactor.changeOps(key, SelectionKey.OP_WRITE);
  }

  /**
   * Writes data to channel.
   */
  private void doWrite(Object pendingWrite, SelectionKey key) throws IOException {
    ByteBuffer buffer = MessagingUtil.serialize(pendingWrite);
    ((SocketChannel) key.channel()).write(buffer);
  }
}
