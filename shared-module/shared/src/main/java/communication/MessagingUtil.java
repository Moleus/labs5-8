package communication;

import communication.packaging.Message;
import communication.packaging.Request;
import communication.packaging.Response;
import exceptions.ReceivedInvalidObjectException;

import java.io.*;
import java.nio.ByteBuffer;

public class MessagingUtil {
  public static Request castRequestWithCheck(Message messageObj) {
    if (!(messageObj instanceof Request)) {
      throw new ReceivedInvalidObjectException(Request.class, messageObj.getClass());
    }
    return (Request) messageObj;
  }

  public static Response castResponseWithCheck(Message messageObj) {
    if (!(messageObj instanceof Response)) {
      throw new ReceivedInvalidObjectException(Response.class, messageObj.getClass());
    }
    return (Response) messageObj;
  }

  public static Message deserialize(ByteBuffer buffer) {
    Object receivedObject;
    try {
      ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
      receivedObject = objectStream.readObject();
    } catch (ClassNotFoundException | IOException e) {
      throw new ReceivedInvalidObjectException(Message.class, e.getMessage());
    }

    if (!(receivedObject instanceof Message)) {
      throw new ReceivedInvalidObjectException(Message.class, receivedObject);
    }
    return (Message) receivedObject;
  }

  public static ByteBuffer serialize(Object message) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096);
    try {
      ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayOutputStream);
      objectStream.writeObject(message);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
  }
}
