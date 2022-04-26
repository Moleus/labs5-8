package server.util;

import communication.packaging.Message;
import communication.packaging.Request;
import exceptions.ReceivedInvalidObjectException;

import java.io.*;
import java.nio.ByteBuffer;

public class MessagingUtil {
  public static Request castWithCheck(Message messageObj) {
    if (!(messageObj instanceof Request request)) {
      throw new ReceivedInvalidObjectException(Request.class, messageObj.getClass());
    }
    return request;
  }

  public static Message readMessage(ByteBuffer buffer) {
    Object receivedObject;
    try {
      ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
      receivedObject = objectStream.readObject();
    } catch (ClassNotFoundException | IOException e) {
      throw new ReceivedInvalidObjectException(Message.class, e.getMessage());
    }

    if (!(receivedObject instanceof Message messageObj)) {
      throw new ReceivedInvalidObjectException(Message.class, receivedObject.getClass());
    }
    return messageObj;
  }

  public static ByteBuffer writeResponse(Object response) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096);
    try {
      ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayOutputStream);
      objectStream.writeObject(response);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
  }
}
