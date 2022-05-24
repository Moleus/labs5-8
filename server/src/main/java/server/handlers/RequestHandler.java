package server.handlers;

import commands.CommandManagerImpl;
import commands.ExecutionPayload;
import communication.MessagingUtil;
import communication.RequestPurpose;
import communication.ResponseCode;
import communication.packaging.BaseResponse;
import communication.packaging.Message;
import communication.packaging.Request;
import communication.packaging.Response;
import exceptions.ReceivedInvalidObjectException;
import lombok.extern.log4j.Log4j2;
import server.authentication.UserManager;
import server.collection.CollectionManager;
import server.exceptions.AuthenticationException;
import server.util.RequestValidator;
import user.User;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.List;
import java.util.Optional;

@Log4j2
public class RequestHandler implements ChannelHandler {

  private final CollectionManager<?> collectionManager;
  private final UserManager userManager;
  private final CommandManagerImpl commandManager;

  private static final List<RequestPurpose> AUTH_REQUIRED_REQUESTS = List.of(RequestPurpose.CHANGE_COLLECTION, RequestPurpose.LOGIN);

  public RequestHandler(CollectionManager<?> collectionManager, UserManager userManager, CommandManagerImpl commandManager) {
    this.collectionManager = collectionManager;
    this.userManager = userManager;
    this.commandManager = commandManager;
  }

  @Override
  public void handleChannelRead(ChannelWrapper channel, ByteBuffer byteBuffer, SelectionKey key) {
    Message message = MessagingUtil.deserialize(byteBuffer);
    Request request = MessagingUtil.castRequestWithCheck(message);
    log.debug("Handling new request: " + request.getPurpose());
    ResponseCode responseCode = ResponseCode.SUCCESS;
    Object result = null;
    try {
      result = getResult(request);
    } catch (AuthenticationException e) {
      log.info("Received request with invalid credentials: " + e.getMessage());
      responseCode = ResponseCode.AUTH_FAILED;
    } catch (ReceivedInvalidObjectException e) {
      log.info("Received request with invalid payload: " + e.getMessage());
      responseCode = ResponseCode.INVALID_PAYLOAD;
    }
    Response response = BaseResponse.of(request.getPurpose(), responseCode, result);
    channel.write(response, key);
  }

  private Object getResult(Request request) {
    RequestPurpose purpose = request.getPurpose();
    Optional<User> user = request.getUser();
    if (AUTH_REQUIRED_REQUESTS.contains(purpose)) {
      request.getUser().filter(userManager::areCredentialsValid).orElseThrow(AuthenticationException::new);
    }
    switch (purpose) {
      case CHANGE_COLLECTION: return request.getPayload()
          .filter(RequestValidator::isExecutable)
          .map(ExecutionPayload.class::cast)
          .map(payload -> addUser(payload, user.orElseThrow(AuthenticationException::new)))
          .map(commandManager::executeCommand)
          .orElseThrow(ReceivedInvalidObjectException::new);
      case INIT_COLLECTION: return collectionManager.getFullCollection();
      case GET_CHANGELIST: return request.getPayload()
          .filter(RequestValidator::isLong)
          .map(Long.class::cast)
          .map(collectionManager::getChangesNewerThan)
          .orElseThrow(ReceivedInvalidObjectException::new);
      case GET_COMMANDS: return commandManager.getUserAccessibleCommandsInfo();
      case LOGIN: return request.getUser()
          .filter(userManager::areCredentialsValid)
          .orElseThrow(AuthenticationException::new);
      case REGISTER: return request.getUser()
          .filter(userManager::isUserUnique)
          .map(userManager::save)
          .orElseThrow(AuthenticationException::new);
    }
    throw new IllegalArgumentException("No such purpose: " + purpose);
  }

  private ExecutionPayload addUser(ExecutionPayload payload, User user) {
    payload.setUser(user);
    return payload;
  }
}
