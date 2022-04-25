package server.commands.pcommands;

import commands.*;
import model.data.Model;
import server.collection.CollectionManager;

import static commands.ExecutionMode.SERVER;

/**
 * Saves user collection in storage
 *
 * @deprecated This class is no longer acceptable to save a collection.
 * New entities are directly inserted in DB table and a collection is used as a cache.
 */
@Deprecated(since = "lab7")
public final class Save<T extends Model> extends AbstractCommand {
  private final CollectionManager<T> collectionManager;

  public Save(CollectionManager<T> collectionManager) {
    super(CommandInfo.of("save", "Save collection in a storage", false, 0, CommandType.OTHER, SERVER));
    this.collectionManager = collectionManager;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    return ExecutionResult.valueOf(false, "DEPRECATED");
  }
}