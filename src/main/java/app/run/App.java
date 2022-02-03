package app.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import app.collection.CollectionManager;
import app.commands.CommandManager;
import app.commands.pcommands.*;
import app.exceptions.CollectionCorruptedException;
import app.exceptions.StorageAccessException;
import app.storage.FileStorage;
import app.storage.Storage;
import app.utils.Console;

/*
TODO Ideas:
I. Хранение данных и взаимодействие с ними:
  1. CollectionManager принимает в конструктор (IStorageManager storageManager)
  2. будет 2 класса FileStorageManager и DatabaseStorageManager реализующие интерфейс IStorageManager.
  3. В CollectionManager в конструктор передается IStorageManager. Загрузка данных в коллекцию: storageManager.readCollection();

II. Взаимодействие с пользователем:
  1. CommandManager - вызов соответствующих команд. 
    аргументом передаётся список объектов команд. 
    каждый объект команд парсится и составляется соответствие команды из консоли и объекта команды.

    публичный только executeCommand(String command, String arguments.... )
    
    Добавить к методам вызова команды соответствующую аннотацию, чтобы нельзя было вызвать не тот метод

/*
  Interactive mode:
  Open input stream.
  Read commands from the input stream.
  Call commandHandler.
  In project we have CollectionHandler.
  ResponseOutputter object

  On startup:
  Autofill Collection from file on startup.
*/

/** TODO list:
 * 1. Проверка, что id - это число (https://github.com/1MikhailStepanov1/LabSixClient/issues/10)
 * 2. Чтение из файла элементов с одинаковыми id -> очистка коллекции
 * 3. Не хардкодить НИКАКИЕ конфиги (https://github.com/CrazyChris3310/Lab7/issues/2)
 * 4. Для чтения файлов конфигураций - ОС независимый путь (https://github.com/CrazyChris3310/Lab7/issues/5)
 * 5. Фильтр значений из коллекции через Stream API 
 * 6.  NPE при нажатии ctrl+d (https://github.com/GesuYaro/lab5/issues/8)
 * 7. use StringBuilder in csv parser (https://github.com/DmitriyAgeevP3131/313304/issues/3)
 * 8. В CollectionManager хранить максимальный идентификатор. Обновлять при добавлении
 * 9. Можно сделать ItemFactory и создавать объекты только через неё, храня id - Сделал singleton FlatBuilder со счётчиком id.
 * 10. Можно (и нужно) разбить на более маленькие и их переиспользовать: readInt, readInteger, readNotNullString, readString... (https://github.com/allegator-e/lab5/issues/2)
 * 11. Генерация ID на сервере
 * 12. При чтении из файла проверять дубликаты id
 *
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
 *
 * FIXME list:
 * 1. CommandManager передача экземпляров команд завязанных на него.
 * 2. Генерация scv строки из коллекции
 */
public class App {
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Please, specify path to collection file (single argument).");
      System.exit(1);
    }
    String filePath = args[0];
    Storage storageManager = new FileStorage(filePath);
    CollectionManager collectionManager = new CollectionManager(storageManager);
    try {
      collectionManager.loadCollection();
    } catch (CollectionCorruptedException | StorageAccessException e) {
      System.out.println("Failed to load collection.\n" + e.getMessage());
    }

    CommandManager commandManager = new CommandManager();


    commandManager.registerCommands(
        new Help(commandManager),  // info about accessible commands
        new Info(collectionManager),  // collection type,initDate,NumOfElements
        new Add(collectionManager),  // add new element to collection
        new Update(collectionManager),  // modify existing element in collection
        new RemoveById(collectionManager),  // remove from collection
        new Clear(collectionManager),  // remove all elements from collection
        new Save(collectionManager, storageManager),  // save collection in storage
        new ExecuteScript(commandManager),  // run all commands from file
        new Exit(),  // exit without saving
        new AddIfMax(collectionManager),  // add new element if it's the greatest
        new AddIfMin(collectionManager),  // add new element if it's the least
        new RemoveLower(collectionManager),  // remove all less than given
        new FilterContainsName(collectionManager),  // print elements with this string in names
        new PrintUniqueNumberOfRooms(collectionManager),  // print set of 'numberOfRooms' values
        new PrintFieldDescendingNew(collectionManager)  // print descending sorted 'new' values
    );

    Console console = new Console(commandManager, commandManager.getUserAccessibleCommands());
    try {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
      console.run(bufferedReader);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}