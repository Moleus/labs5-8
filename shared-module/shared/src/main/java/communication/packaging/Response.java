package communication.packaging;

import communication.ResponseCode;

public interface Response extends Message {
  ResponseCode getResponseCode();
}
