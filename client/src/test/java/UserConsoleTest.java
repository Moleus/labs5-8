import utils.Console;
import exceptions.CollectionCorruptedException;
import exceptions.StorageAccessException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.FlatValues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class UserConsoleTest {
  static CommandManager commandManager;
  static CollectionManager collectionManager;

  @BeforeAll
  static void prepare() throws CollectionCorruptedException, StorageAccessException {
    Storage storageManager = new FileStorage("collection.csv", FlatBuilder.createInstance());
    commandManager = new CommandManager();
    collectionManager = new CollectionManager(storageManager);
    collectionManager.loadCollection();
    commandManager.registerCommands(new ExecuteScript(),
                                    new Info(collectionManager),
                                    new Add(collectionManager),
                                    new Update(collectionManager));
  }
  @Test
  void testScriptRun() throws IOException {
    String[] testCommands = {
        "execute_script script2.txt",
        "execute_script script_add.txt",
        "exit"
    };
    BufferedReader br = new BufferedReader(new StringReader(String.join("\n", testCommands)));
    UserConsole userConsole = new UserConsole(br, System.out, commandManager, commandManager.getUserAccessibleCommands());
    userConsole.run();
  }

  @Test
  void testUpdateElement() throws IOException {
    Object[] testInput = {"update 0", FlatValues.NAME, FlatValues.COORD_X, FlatValues.COORD_Y, FlatValues.AREA, FlatValues.ROOMS, FlatValues.HAS_FURNITURE, FlatValues.IS_NEW, FlatValues.VIEW, FlatValues.HOUSE_NAME, FlatValues.HOUSE_YEAR, FlatValues.HOUSE_FLOORS, FlatValues.HOUSE_LIFTS};
    String inpString = Arrays.stream(testInput).map(Object::toString).collect(Collectors.joining(System.lineSeparator()));

    BufferedReader br = new BufferedReader(new StringReader(inpString));
    UserConsole userConsole = new UserConsole(br, System.out, commandManager, commandManager.getUserAccessibleCommands());
    userConsole.run();
  }
}
