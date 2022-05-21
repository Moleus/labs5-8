package user;

import lombok.Data;
import perform.annotations.*;

import java.io.Serializable;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {
  @Id
  private long id;

  @NotNull
  @Unique
  private final String login;

  @NotNull
  private final byte[] password;
}
