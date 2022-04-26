package commands.readers;

import java.io.IOException;

public interface ObjectReader<T> {
  T read() throws IOException;
}
