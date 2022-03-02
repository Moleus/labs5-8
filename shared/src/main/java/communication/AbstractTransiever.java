package communication;

import communication.packaging.Message;
import exceptions.ConnectionIsDownException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

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
  public abstract Message recieve() throws IOException, ClassNotFoundException, ConnectionIsDownException;

  @Override
  public void newSocketChannel(SocketChannel socketChannel) {
    this.socketChannel = socketChannel;
  }

  protected Message readObject() throws IOException, ClassNotFoundException, ConnectionIsDownException {
    byte[] buffer = new byte[4096];
    Object recievedObject;

    int bytesRead = socketChannel.read(ByteBuffer.wrap(buffer));
    if (bytesRead == -1) {
      throw new ConnectionIsDownException("Connection is down");
    }
    if (bytesRead == 0) {
      throw new IOException("Empty stream");
    }

    ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(buffer));
    recievedObject = objectStream.readObject();

    if (!(recievedObject instanceof Message messageObj)) {
      throw new InvalidObjectException("Recieved object is not an instance of Message");
    }
    return messageObj;
  }
}