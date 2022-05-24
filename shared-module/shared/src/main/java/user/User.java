package user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import perform.annotations.*;

import java.io.Serializable;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User implements Serializable {
  @Id
  private long id;

  @NotNull
  @Unique
  private String login;

  @NotNull
  private byte[] password;

  public User(String login, byte[] password) {
    this.login = login;
    this.password = password;
  }
}
