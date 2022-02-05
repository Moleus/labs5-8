package app.utils;

import app.collection.CollectionManager;
import app.collection.FlatBuilder;
import app.commands.CommandManager;
import app.commands.pcommands.Add;
import app.commands.pcommands.ExecuteScript;
import app.commands.pcommands.Info;
import app.commands.pcommands.Update;
import app.exceptions.CollectionCorruptedException;
import app.exceptions.StorageAccessException;
import app.storage.FileStorage;
import app.storage.Storage;
import app.utils.Console;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.stream.Collectors;

import static util.FlatValues.*;

public class ConsoleTest {
  static CommandManager commandManager;
  static CollectionManager collectionManager;

  @BeforeAll
  static void prepare() throws CollectionCorruptedException, StorageAccessException {
    Storage storageManager = new FileStorage("collection.csv", FlatBuilder.createInstance());
    commandManager = new CommandManager();
    collectionManager = new CollectionManager(storageManager);
    collectionManager.loadCollection();
    commandManager.registerCommands(new ExecuteScript(commandManager),
                                    new Info(collectionManager),
                                    new Add(collectionManager),
                                    new Update(collectionManager));
  }
  @Test
  void testScriptRun() throws IOException {
    String[] testCommands = {
        "execute_script script2.txt",
        "execute_script script_add.txt"
    };
    BufferedReader br = new BufferedReader(new StringReader(String.join("\n", testCommands)));
    Console console = new Console(br, System.out, commandManager, commandManager.getUserAccessibleCommands());
    console.run();
  }

  @Test
  void testUpdateElement() throws IOException {
    Object[] testInput = {"update 0", NAME, COORD_X, COORD_Y, AREA, ROOMS, HAS_FURNITURE, IS_NEW, VIEW, HOUSE_NAME, HOUSE_YEAR, HOUSE_FLOORS, HOUSE_LIFTS};
    String inpString = Arrays.stream(testInput).map(Object::toString).collect(Collectors.joining(System.lineSeparator()));

    BufferedReader br = new BufferedReader(new StringReader(inpString));
    Console console = new Console(br, System.out, commandManager, commandManager.getUserAccessibleCommands());
    console.run();
  }
}
