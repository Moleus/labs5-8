package app.commands.pcommands;

import app.collection.CollectionManager;
import app.collection.FlatBuilder;
import app.collection.data.Flat;
import app.commands.*;
import app.exceptions.ElementNotFoundException;
import app.exceptions.InvalidDataValues;

import java.time.LocalDate;

public final class Update extends AbstractCommand {
  private final CollectionManager collectionManager;
  public Update(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("update", "Modify an existing element in collection", true, 1, true));
    this.collectionManager = collectionManager;
  }

  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    String idStr = payload.getInlineArg();
    int id;
    try {
      id = Integer.parseInt(idStr);
    }
    catch (NumberFormatException e) {
      return ExecutionResult.valueOf(false, "Id should be an integer");
    }

    Object[] dataValues = payload.getDataValues();

    try {
      Flat oldFlat = collectionManager.getById(id);
      LocalDate creationDate = oldFlat.getCreationDate();
      Flat newFlat = FlatBuilder.getInstance().buildWithCustomIdAndDate(id, creationDate, dataValues);
      collectionManager.removeById(id);
      collectionManager.add(newFlat);
    } catch (InvalidDataValues e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    } catch (ElementNotFoundException e) {
      return ExecutionResult.valueOf(false, "Element with id " + id + " doesn't exist");
    }

    return ExecutionResult.valueOf(true, "item updated");
  }
}