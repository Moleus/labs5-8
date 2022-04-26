package communication;

import communication.packaging.Message;
import exceptions.ReceivedInvalidObjectException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Optional;

public abstract class AbstractTransiever implements Transceiver {
  protected SocketChannel socketChannel;

  public AbstractTransiever(SocketChannel socketChannel) {
    this.socketChannel = socketChannel;
  }

  @Override
  public void send(Message message) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096);

    ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayOutputStream);
    objectStream.writeObject(message);

    ByteBuffer byteBuffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
    socketChannel.write(byteBuffer);
  }

  @Override
  public abstract Optional<? extends Message> recieve() throws IOException, ReceivedInvalidObjectException;

  @Override
  public void newSocketChannel(SocketChannel socketChannel) {
    this.socketChannel = socketChannel;
  }

  protected Optional<Message> readObject() throws IOException {
    byte[] buffer = new byte[4096];
    Object recievedObject;

    int bytesRead = socketChannel.read(ByteBuffer.wrap(buffer));
    if (bytesRead == 0) {
      return Optional.empty();
    }
    //  0 = empty stream when non-blocking
    // -1 = disconnected

    ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(buffer));
    try {
      recievedObject = objectStream.readObject();
    } catch (ClassNotFoundException e) {
      throw new ReceivedInvalidObjectException(Message.class, e.getMessage());
    }

    if (!(recievedObject instanceof Message messageObj)) {
      throw new ReceivedInvalidObjectException(Message.class, recievedObject.getClass());
    }
    return Optional.of(messageObj);
  }

}