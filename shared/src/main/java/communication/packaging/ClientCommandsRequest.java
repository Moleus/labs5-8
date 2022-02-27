package communication.packaging;

import communication.RequestPurpose;

import java.util.Optional;

public class ClientCommandsRequest implements Request {
  private final RequestPurpose purpose = RequestPurpose.GET_COMMANDS;

  @Override
  public RequestPurpose getPurpose() {
    return purpose;
  }

  @Override
  public Optional<Object> getPayload() {
    return Optional.empty();
  }
}
