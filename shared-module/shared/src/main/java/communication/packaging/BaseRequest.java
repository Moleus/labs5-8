package communication.packaging;

import communication.RequestPurpose;
import lombok.Data;
import user.User;

import java.util.Optional;

@Data(staticConstructor = "of")
public class BaseRequest implements Request {
  private static final long serialVersionUID = 8_0;
  private final RequestPurpose purpose;
  private final Object payload;
  public final User user;

  @Override
  public Optional<Object> getPayload() {
    return Optional.ofNullable(payload);
  }

  @Override
  public Optional<User> getUser() {
    return Optional.ofNullable(user);
  }
}
