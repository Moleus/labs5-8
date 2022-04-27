package server.handlers;

import commands.CommandManager;
import commands.ExecutionPayload;
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
import server.util.MessagingUtil;
import server.util.RequestValidator;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.List;

@Log4j2
public class RequestHandler implements ChannelHandler {

  private final CollectionManager<?> collectionManager;
  private final UserManager userManager;
  private final CommandManager commandManager;

  private static final List<RequestPurpose> AUTH_REQUIRED_REQUESTS = List.of(RequestPurpose.CHANGE_COLLECTION, RequestPurpose.LOGIN);

  public RequestHandler(CollectionManager<?> collectionManager, UserManager userManager, CommandManager commandManager) {
    this.collectionManager = collectionManager;
    this.userManager = userManager;
    this.commandManager = commandManager;
  }

  @Override
  public void handleChannelRead(ChannelWrapper channel, Object readObject, SelectionKey key) {
    Message message = MessagingUtil.readMessage((ByteBuffer) readObject);
    Request request = MessagingUtil.castWithCheck(message);
    log.debug("Handling new request: " + request.getPurpose());
    ResponseCode responseCode = ResponseCode.SUCCESS;
    Object result = null;
    try {
      result = getResult(request);
    } catch (AuthenticationException e) {
      log.info("Invalid user credentials: " + request.getUser());
      responseCode = ResponseCode.AUTH_FAILED;
    } catch (ReceivedInvalidObjectException e) {
      log.info("Invalid payload: " + request.getPayload());
      responseCode = ResponseCode.INVALID_PAYLOAD;
    }
    Response response = BaseResponse.of(request.getPurpose(), responseCode, result);
    channel.write(response, key);
  }

  private Object getResult(Request request) {
    RequestPurpose purpose = request.getPurpose();
    if (AUTH_REQUIRED_REQUESTS.contains(purpose)) {
      request.getUser().filter(userManager::areCredentialsValid).orElseThrow(AuthenticationException::new);
    }
    return switch (purpose) {
      case CHANGE_COLLECTION -> request.getPayload()
          .filter(RequestValidator::isExecutable)
          .map(ExecutionPayload.class::cast)
          .map(commandManager::executeCommand)
          .orElseThrow(ReceivedInvalidObjectException::new);
      case INIT_COLLECTION -> collectionManager.getFullCollection();
      case GET_CHANGELIST -> request.getPayload()
          .filter(RequestValidator::isLong)
          .map(Long.class::cast)
          .map(collectionManager::getChangesNewerThan)
          .orElseThrow(ReceivedInvalidObjectException::new);
      case GET_COMMANDS -> commandManager.getUseraccessibleCommandsInfo();
      case LOGIN -> request.getUser()
          .filter(userManager::areCredentialsValid)
          .orElseThrow(AuthenticationException::new);
      case REGISTER -> request.getUser()
          .filter(userManager::isUserUnique)
          .map(userManager::save)
          .orElseThrow(AuthenticationException::new);
    };
  }
}
