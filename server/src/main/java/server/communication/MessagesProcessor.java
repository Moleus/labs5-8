package server.communication;

import commands.ExecutionPayload;
import communication.RequestPurpose;
import communication.ResponseCode;
import communication.packaging.BaseResponse;
import communication.packaging.Request;
import communication.packaging.Response;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MessagesProcessor {
  private final Request processingMessage;
  private final RequestPurpose purpose;
  private ResponseCode responseCode = ResponseCode.SUCCESS;
  private Object responsePayload = null;

  public MessagesProcessor(Request request) {
    this.processingMessage = request;
    this.purpose = request.getPurpose();
  }

  public static MessagesProcessor of(Request request) {
    return new MessagesProcessor(request);
  }

  public void setResponsePayload(Object payload) {
    this.responsePayload = payload;
  }

  public boolean isPayloadValid() {
    boolean isValid = switch (purpose) {
      case EXECUTE -> processingMessage.getPayload().map(this::isValidExecuteValues).orElse(false);
      case GET_COMMANDS -> processingMessage.getPayload().isEmpty();
    };

    if (!isValid) {
      responseCode = ResponseCode.INVALID_PAYLOAD;
    }
    return isValid;
  }

  private boolean isValidExecuteValues(Object values) {
    return (values instanceof ExecutionPayload);
  }

  public Response buildResponse() {
    return createResponse(purpose, responseCode, responsePayload);
  }

  private Response createResponse(RequestPurpose purpose, ResponseCode responseCode, Object payload) {
    log.debug("Payload is: {}", payload);
    return BaseResponse.of(purpose, responseCode, payload);
  }

}
