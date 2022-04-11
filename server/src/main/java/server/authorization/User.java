package server.authorization;

import lombok.Data;
import perform.annotations.*;

@Data
@Entity
@Table(name = "users")
public class User {
  @Id
  private long id;

  @NotNull
  @Unique
  private final String login;

  @NotNull
  private final char[] password;
}
