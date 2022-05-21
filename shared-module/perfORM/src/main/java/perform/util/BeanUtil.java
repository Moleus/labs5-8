package perform.util;

import perform.exception.BeanIntrospectionException;
import perform.exception.ClassInstantiationException;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BeanUtil {
  public static <T> T instantiateClass(Class<T> clazz) {
    Constructor<T> constructor;
    try {
      constructor = clazz.getDeclaredConstructor();
    } catch (NoSuchMethodException e) {
      throw new ClassInstantiationException(clazz, "No default constructor found", e);
    }
    return instantiateClass(constructor);
  }

  private static <T> T instantiateClass(Constructor<T> constructor) {
    ReflectionUtil.makeAccessible(constructor);
    try {
      return constructor.newInstance();
    } catch (InstantiationException e) {
      throw new ClassInstantiationException(constructor, "Entity Instantiation error", e);
    } catch (IllegalAccessException e) {
      throw new ClassInstantiationException(constructor, "Can't access class", e);
    } catch (InvocationTargetException e) {
      throw new ClassInstantiationException(constructor, "Failed to instantiate class", e);
    }
  }

  public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
    try {
      return Introspector.getBeanInfo(clazz).getPropertyDescriptors();
    } catch (IntrospectionException e) {
      throw new BeanIntrospectionException(clazz, "tried to get bean info", e);
    }
  }
}