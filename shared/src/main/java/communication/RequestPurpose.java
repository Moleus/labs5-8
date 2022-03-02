package communication;

import java.io.Serializable;

public enum RequestPurpose implements Serializable {
  EXECUTE,
  UPDATE_COLLECTION,
  GET_COMMANDS
}
