package server.collection;

import model.ModelDto;
import model.data.Flat;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;

public class DtoToModelMapper {
  public static <D extends ModelDto, E> E fromDto(D dtoObject) {
    ModelMapper modelMapper = new ModelMapper();
    Type entityType = new TypeToken<Flat>() {
    }.getType();
    return modelMapper.map(dtoObject, entityType);
  }
}
