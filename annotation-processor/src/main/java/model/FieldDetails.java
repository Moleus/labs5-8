package model;

public record FieldDetails<T>(Class<T> type, String description,
                              ThrowableSetter<T> setterMethod) {
}
