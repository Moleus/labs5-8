package communication.packaging;

import commands.ExecutionPayload;
import communication.RequestPurpose;
import lombok.Data;

import java.io.Serial;
import java.util.Optional;

@Data(staticConstructor = "of")
public class ExecutionRequest implements Request  {
  @Serial
  private static final long serialVersionUID = 101L;
  private final RequestPurpose purpose = RequestPurpose.EXECUTE;
  private final ExecutionPayload payload;

  @Override
  public Optional<Object> getPayload() {
    return Optional.ofNullable(payload);
  }
}
