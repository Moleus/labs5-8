package model;


import exceptions.ValueConstraintsException;

@FunctionalInterface
public interface ThrowableSetter<T> {
  void accept(T t) throws ValueConstraintsException;
}
