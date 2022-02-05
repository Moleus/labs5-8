package app.utils;

public enum KeyBindings {
  HISTORY_UP("history_up", "k"),
  HISTORY_DOWN("history_down", "j");

  private String name;
  private String binding;

  KeyBindings(String name, String binding) {
    this.name = name;
    this.binding = binding;
  }

  public String getName() {
    return name;
  }

  public String getBinding() {
    return binding;
  }

  public void setBinding(String newBinding) {
    this.binding = newBinding;
  }
}
