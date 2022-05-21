package communication.packaging;

import communication.RequestPurpose;

import java.io.Serializable;
import java.util.Optional;

public interface Message extends Serializable {
  RequestPurpose getPurpose();
  Optional<Object> getPayload();
}