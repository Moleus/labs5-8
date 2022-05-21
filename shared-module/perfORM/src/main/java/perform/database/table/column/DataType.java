package perform.database.table.column;

import lombok.Data;
import perform.util.StringUtil;

@Data(staticConstructor = "of")
public class DataType {
  private final String name;
  private Integer length;

  @Override
  public String toString() {
    String lengthText = StringUtil.addBrackets(length);
    return StringUtil.join(" ", name, lengthText);
  }
}