package perform.database.table.column;

import lombok.Data;
import perform.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

@Data(staticConstructor = "of")
public class Constraint {
  private final String name;
  private final List<String> parameters = new ArrayList<>();
  private final List<String> expressions = new ArrayList<>();

  public void addParameter(String param) {
    parameters.add(param);
  }

  public void addExpression(String expression) {
    expressions.add(expression);
  }

  @Override
  public String toString() {
    String joinedParams = StringUtil.join(" ", parameters);
    String joinedConstraints = StringUtil.addBrackets(String.join(" AND ", expressions));
    return StringUtil.join(" ", name, joinedParams, joinedConstraints);
  }
}