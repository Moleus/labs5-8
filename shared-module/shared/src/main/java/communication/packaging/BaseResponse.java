package communication.packaging;

import communication.RequestPurpose;
import communication.ResponseCode;
import lombok.Data;

import java.util.Optional;

@Data(staticConstructor="of")
public class BaseResponse implements Response {
  private final RequestPurpose purpose;
  private final ResponseCode responseCode;
  private final Object payload;

  @Override
  public RequestPurpose getPurpose() {
    return this.purpose;
  }

  @Override
  public Optional<Object> getPayload() {
    return Optional.ofNullable(payload);
  }
}
