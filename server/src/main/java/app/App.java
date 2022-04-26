package app;

import commands.CommandManager;
import lombok.extern.log4j.Log4j2;
import model.data.Flat;
import perform.bootstrap.Bootstrap;
import server.authentication.UserManager;
import server.collection.CollectionManager;
import server.collection.FlatsCollectionManager;
import server.commands.pcommands.*;
import server.generated.repository.FlatRepository;
import server.generated.repository.UserRepository;
import user.User;

import java.io.IOException;

/* TODO list:
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
 * 3. edit separate fields of object in collection
 *
 *
 *
 * Write-through
 * запись в бд и в память
 * чтение из памяти
 *
 * синтетический адаптер между запросами к бд builderWrapper
 */

/**
 * Main app class. Entry point.
 */
@Log4j2
public class App {
  public static void main(String[] args) {
    Bootstrap bootstrap = new Bootstrap();
    FlatRepository flatRepository = bootstrap.getRepository(Flat.class);
    UserRepository userRepository = bootstrap.getRepository(User.class);

    UserManager userManager = new UserManager(userRepository);
    CollectionManager<Flat> collectionManager = new FlatsCollectionManager(flatRepository, userManager);

    collectionManager.loadCollection();

    CommandManager commandManager = new CommandManager();
    // to server:
    // 1. command (String) with objects array
    // 2. command (String) with inline arg
    // 3.
    // when connection initialized server sends accessible commands to client.
    // check if port is free


    // Save - only server

    commandManager.registerCommands(
        new Add<>(collectionManager),  // add new element to collection
        new Update<>(collectionManager),  // modify existing element in collection
        new RemoveById<>(collectionManager),  // remove from collection
        new Clear<>(collectionManager),  // remove all elements from collection
        new AddIfMax<>(collectionManager),  // add new element if it's the greatest
        new AddIfMin<>(collectionManager),  // add new element if it's the least
        new RemoveLower<>(collectionManager)  // remove all less than given
    );

    try {
      Server server = new Server(2222, commandManager, collectionManager, userManager);
      server.run();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}