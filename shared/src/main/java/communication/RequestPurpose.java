package communication;

import java.io.Serializable;

public enum RequestPurpose implements Serializable {
  EXECUTE,
  INIT_COLLECTION,
  UPDATE_COLLECTION,
  GET_COMMANDS
}
