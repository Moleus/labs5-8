package communication;

import communication.packaging.Message;
import exceptions.ReceivedInvalidObjectException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Optional;

public abstract class AbstractTransceiver implements Transceiver {
  protected SocketChannel socketChannel;

  public AbstractTransceiver(SocketChannel socketChannel) {
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
  public abstract Optional<? extends Message> receive() throws IOException, ReceivedInvalidObjectException;

  @Override
  public void newSocketChannel(SocketChannel socketChannel) {
    this.socketChannel = socketChannel;
  }

  protected Optional<Message> readObject() throws IOException {
    byte[] buffer = new byte[16384];
    Object receivedObject;

    int bytesRead = socketChannel.read(ByteBuffer.wrap(buffer));
    if (bytesRead == 0) {
      return Optional.empty();
    }
    //  0 = empty stream when non-blocking
    // -1 = disconnected

    ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(buffer));
    try {
      receivedObject = objectStream.readObject();
    } catch (ClassNotFoundException e) {
      throw new ReceivedInvalidObjectException(Message.class, e.getMessage());
    }

    if (!(receivedObject instanceof Message)) {
      throw new ReceivedInvalidObjectException(Message.class, receivedObject.getClass());
    }
    return Optional.of((Message) receivedObject);
  }
}