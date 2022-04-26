package commands.readers;

import exceptions.ValueConstraintsException;
import exceptions.ValueFormatException;
import model.ModelDto;
import model.builder.BuilderWrapper;
import model.builder.ModelDtoBuilderWrapper;
import model.data.ModelDtoBuilder;

import java.io.IOException;

public class DtoReader implements ObjectReader<ModelDto> {
  private final BuilderWrapper<ModelDto> builderWrapper = new ModelDtoBuilderWrapper(new ModelDtoBuilder());
  private final IOSource ioSource;

  public DtoReader(IOSource IOSource) {
    this.ioSource = IOSource;
  }

  @Override
  public ModelDto read() throws IOException {
    int fieldsCount = builderWrapper.getFieldsCount();
    builderWrapper.setPosition(0);

    while (builderWrapper.getPosition() < fieldsCount) {
      ioSource.print("Enter " + builderWrapper.getDescription() + ": ");
      String line = ioSource.readLine();
      try {
        builderWrapper.setValue(line);
      } catch (ValueFormatException | ValueConstraintsException e) {
        System.out.println(e.getMessage());
        continue;
      }
      builderWrapper.step();
    }

    return builderWrapper.build();
  }
}
