package communication;

import java.io.Serializable;

public enum RequestPurpose implements Serializable {
  CHANGE_COLLECTION,
  INIT_COLLECTION,
  GET_CHANGELIST,
  GET_COMMANDS,
  LOGIN,
  REGISTER
}
