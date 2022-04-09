package app;

import commands.CommandManager;
import lombok.extern.log4j.Log4j2;
import model.Model;
import model.builder.BuilderWrapper;
import model.builder.ModelBuilderWrapper;
import server.collection.CollectionManager;
import server.commands.pcommands.*;
import server.exceptions.CollectionCorruptedException;
import server.exceptions.StorageAccessException;
import server.storage.FileStorage;
import server.storage.Storage;

import java.io.IOException;

/* TODO list:
 * LAB6:
 * 1. Обработка отключения сервера (https://github.com/1MikhailStepanov1/LabSixClient/issues/14)
 * 2. Response class (https://github.com/1MikhailStepanov1/LabSixClient/issues/6) (https://github.com/rimnvd/lab6/issues/3)
 * 3. Реализовать Response с помощью Stream API (https://github.com/rimnvd/lab6/issues/5)
 * 4. Не использовать Thread (https://github.com/1MikhailStepanov1/LabSixServer/issues/4)
 *
 * LAB7:
 * 1. Хэшировать пароль (https://github.com/CrazyChris3310/Lab7/issues/4)
 * 2. Блокировка не имеет смысла (https://github.com/CrazyChris3310/Lab7/issues/3)
 * 3. Проверять логин и пароль перед каждой командой
 * 4. Описание команд будет дублироваться описанием из файлов с локализациями
 * 5. Не хардкодить НИКАКИЕ конфиги (https://github.com/CrazyChris3310/Lab7/issues/2)
 * 6. Для чтения файлов конфигураций - ОС независимый путь (https://github.com/CrazyChris3310/Lab7/issues/5)
 *
 * Important:
 * 1. in lab8 client should have full information about stored collection.
 * 2. remove any owned entry from collection from gui
 * 3. edit seperate fields of object in collection
 */

/**
 * Main app class. Entry point.
 */
@Log4j2
public class App {
  public static void main(String[] args) {
    FlatRepository repository = Bootstrap.getFlatRepository();
    CollectionManager<Flat> collectionManager = new GenericCollectionManager<>(new FlatChangesTracker(), repository);

    try {
      collectionManager.loadCollection();
    } catch (StorageAccessException e) {
      System.out.printf("Failed to load collection.%n%s %n", e.getMessage());
    }

    CommandManager commandManager = new CommandManager();
    // to server:
    // 1. command (String) with objects array
    // 2. command (String) with inline arg
    // 3.
    // when connection initialized server sends accessible commands to client.
    // check if port is free


    // Save - only server

    commandManager.registerCommands(
        new Add(collectionManager),  // add new element to collection
        new Update(collectionManager),  // modify existing element in collection
        new RemoveById(collectionManager),  // remove from collection
        new Clear(collectionManager),  // remove all elements from collection
        new Save(collectionManager),  // save collection in storage
        new AddIfMax(collectionManager),  // add new element if it's the greatest
        new AddIfMin(collectionManager),  // add new element if it's the least
        new RemoveLower(collectionManager)  // remove all less than given
    );

    try {
      Server server = new Server(2222, commandManager, collectionManager);
      server.run();
      collectionManager.saveCollection();
      log.info("Collection saved successfully");
    } catch (IOException e) {
      e.printStackTrace();
    } catch (StorageAccessException e) {
      log.error("Failed to save collection: {}", e.getMessage());
    }
  }
}