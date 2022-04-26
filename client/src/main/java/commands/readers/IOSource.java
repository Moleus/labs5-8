package commands.readers;

import java.io.IOException;

public interface IOSource {
  String readLine() throws IOException;

  byte[] readPassword();

  void print(String text);
}
