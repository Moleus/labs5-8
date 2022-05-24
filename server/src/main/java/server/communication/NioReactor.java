package server.communication;

import lombok.extern.log4j.Log4j2;
import server.handlers.ChannelWrapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Log4j2
public class NioReactor {
  private final Selector selector;
  private final Dispatcher dispatcher;

  private final Queue<Runnable> pendingCommands = new ConcurrentLinkedDeque<>();
  private final ExecutorService reactorMain = Executors.newSingleThreadExecutor();

  public NioReactor(Dispatcher dispatcher) throws IOException {
    this.dispatcher = dispatcher;
    this.selector = Selector.open();
  }

  public NioReactor registerChannel(ChannelWrapper channel) throws IOException {
    SelectionKey key = channel.getJavaChannel().register(selector, SelectionKey.OP_ACCEPT);
    key.attach(channel);
    channel.setReactor(this);
    return this;
  }

  public void start() {
    reactorMain.execute(() -> {
      try {
        log.info("Started event loop");
        eventLoop();
      } catch (IOException e) {
        log.info("Event loop exited with exception: ", e);
      }
    });
  }

  public void stop() throws InterruptedException, IOException {
    reactorMain.shutdown();
    selector.wakeup();
    if (!reactorMain.awaitTermination(2, TimeUnit.SECONDS)) {
      reactorMain.shutdownNow();
    }
    selector.close();
    log.info("Reactor stopped");
  }

  private void eventLoop() throws IOException {
    while (!Thread.interrupted()) {
      processPendingCommands();
      selector.select();

      Set<SelectionKey> keys = selector.selectedKeys();
      for (Iterator<SelectionKey> it = keys.iterator(); it.hasNext(); ) {
        SelectionKey key = it.next();
        if (!key.isValid()) {
          it.remove();
          continue;
        }
        handleSelectedKey(key);
      }
      keys.clear();
    }
  }

  private void handleSelectedKey(SelectionKey key) throws IOException {
    if (key.isAcceptable()) {
      onChannelAcceptable(key);
    } else if (key.isReadable()) {
      onChannelReadable(key);
    } else if (key.isWritable()) {
      onChannelWritable(key);
    }
  }

  private void onChannelAcceptable(SelectionKey key) throws IOException {
    log.debug("New client connected");
    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
    SocketChannel socketChannel = serverSocketChannel.accept();
    socketChannel.configureBlocking(false);
    SelectionKey readKey = socketChannel.register(selector, SelectionKey.OP_READ);
    readKey.attach(key.attachment());
  }

  private void onChannelReadable(SelectionKey key) {
    try {
      ByteBuffer objectBuffer = ((ChannelWrapper) key.attachment()).read(key);
      if (objectBuffer == null) return;
      dispatchReadEvent(key, objectBuffer);
    } catch (IOException e) {
      try {
        key.channel().close();
      } catch (IOException e1) {
        log.error("error closing channel", e1);
      }
    }
  }

  private void dispatchReadEvent(SelectionKey key, ByteBuffer objectBuffer) {
    dispatcher.onChannelReadEvent((ChannelWrapper) key.attachment(), objectBuffer, key);
  }

  private static void onChannelWritable(SelectionKey key) throws IOException {
    ChannelWrapper channel = (ChannelWrapper) key.attachment();
    channel.flush(key);
  }


  private void processPendingCommands() {
    Iterator<Runnable> iterator = pendingCommands.iterator();
    while (iterator.hasNext()) {
      Runnable command = iterator.next();
      command.run();
      iterator.remove();
    }
  }

  /**
   * Queues the change of interested Ops for specific channel
   */
  public void changeOps(SelectionKey key, int interestedOps) {
    pendingCommands.add(new ChangeKeyOpsCommand(key, interestedOps));
    selector.wakeup();
  }

  static class ChangeKeyOpsCommand implements Runnable {
    private final SelectionKey key;
    private final int interestedOps;

    public ChangeKeyOpsCommand(SelectionKey key, int interestedOps) {
      this.key = key;
      this.interestedOps = interestedOps;
    }

    public void run() {
      try {
        key.interestOps(interestedOps);
      } catch (CancelledKeyException e) {
        log.info("Client disconnected");
      }
    }
  }
}
