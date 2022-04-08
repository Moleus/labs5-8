package processor.poet;

import com.squareup.javapoet.TypeSpec;

public interface BuilderPoet {
  TypeSpec getGeneratedBuilderClass();
}
