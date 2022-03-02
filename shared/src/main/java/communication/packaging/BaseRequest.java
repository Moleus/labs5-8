package communication.packaging;

import communication.RequestPurpose;
import lombok.Data;

import java.util.Optional;

@Data(staticConstructor = "of")
public class BaseRequest implements Request {
  private final RequestPurpose purpose;
  private final Object payload;

  @Override
  public Optional<Object> getPayload() {
    return Optional.ofNullable(payload);
  }
}
