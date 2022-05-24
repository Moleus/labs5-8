package server.communication;

import server.handlers.ChannelWrapper;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolDispatcher implements Dispatcher {

  private final ExecutorService executorService;

  public ThreadPoolDispatcher(int poolSize) {
    this.executorService = Executors.newFixedThreadPool(poolSize);
  }

  @Override
  public void onChannelReadEvent(ChannelWrapper channel, ByteBuffer byteBuffer, SelectionKey key) {
    executorService.execute(() -> channel.getHandler().handleChannelRead(channel, byteBuffer, key));
  }

  @Override
  public void stop() {
    executorService.shutdownNow();
  }
}
