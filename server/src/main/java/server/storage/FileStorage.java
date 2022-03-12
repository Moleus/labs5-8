package server.storage;

import exceptions.ValueConstraintsException;
import exceptions.ValueFormatException;
import model.Model;
import model.builder.BuilderWrapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import server.exceptions.CollectionCorruptedException;
import server.exceptions.StorageAccessException;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides API to load or save a collection from/to file.
 */
public class FileStorage implements Storage {
  private final File file;
  private final BuilderWrapper<Model> builderWrapper;

  public FileStorage(String filePath, BuilderWrapper<Model> flatBuilder) {
    this.file = new File(filePath);
    this.builderWrapper = flatBuilder;
  }

  /**
   * Load collection from a file.
   *
   * @see Storage#loadCollection()
   */
  @Override
  public AbstractMap.SimpleEntry<LocalDateTime, Set<Model>> loadCollection() throws CollectionCorruptedException, StorageAccessException {
    List<CSVRecord> records;
    try {
      String collectionLines = readFromFile();
      CSVParser parser = CSVParser.parse(collectionLines, CSVFormat.INFORMIX_UNLOAD);
      records = parser.getRecords();
    } catch (FileNotFoundException e) {
      throw new StorageAccessException("Can't find a file to load collection");
    } catch (IOException e) {
      throw new StorageAccessException("Failed to read collection from file. IO error: " + e.getMessage());
    } catch (SecurityException e) {
      throw new StorageAccessException("Permissions denied while loading collection. Check file read permissions");
    }

    LocalDateTime creationDate = popDateOrNow(records);
    Set<Model> collection = fillCollectionFrom(records);

    return new AbstractMap.SimpleEntry<>(creationDate, collection);
  }

  private String readFromFile() throws CollectionCorruptedException, IOException {
    final int MAX_DATA_LENGTH = 1000;
    FileInputStream inputStream = new FileInputStream(file);

    InputStreamReader reader = new InputStreamReader(new BufferedInputStream(inputStream));
    String data = readInput(reader, MAX_DATA_LENGTH).trim();
    if (data.length() == MAX_DATA_LENGTH) {
      throw new CollectionCorruptedException("File is too large.");
    }
    if (data.isEmpty()) {
      throw new CollectionCorruptedException("File is empty.");
    }
    return data;
  }

  private String readInput(InputStreamReader reader, int length) throws IOException {
    char[] chars = new char[length];
    if (reader.read(chars, 0, length) == -1) return "";
    return String.valueOf(chars);
  }

  private LocalDateTime popDateOrNow(List<CSVRecord> records) {
    LocalDateTime creationDate = LocalDateTime.now();
    Optional<LocalDateTime> creationDateOp = readDateFromRecord(records.get(0));
    if (creationDateOp.isPresent()) {
      creationDate = creationDateOp.get();
      records.remove(0);
    }
    return creationDate;
  }

  private Optional<LocalDateTime> readDateFromRecord(CSVRecord record) {
    try {
      return Optional.of(LocalDateTime.parse(record.get(0)));
    } catch (DateTimeParseException e) {
      return Optional.empty();
    }
  }

  private Set<Model> fillCollectionFrom(List<CSVRecord> records) throws CollectionCorruptedException {
    Set<Model> collection = new HashSet<>();
    for (CSVRecord model : records) {
      String fieldsByLine = model.stream().collect(Collectors.joining(System.lineSeparator()));
      BufferedReader bfReader = new BufferedReader(new StringReader(fieldsByLine));
      Model newModel;
      try {
        newModel = readModelFrom(bfReader);
      } catch (IOException | ValueConstraintsException | ValueFormatException e) {
        throw new CollectionCorruptedException("File is corrupted. Error: " + e.getMessage());
      }
      if (!collection.add(newModel)) {
        throw new CollectionCorruptedException("Collection contains duplicating ids");
      }
    }
    return collection;
  }

  private Model readModelFrom(BufferedReader reader) throws IOException, ValueConstraintsException, ValueFormatException {
    int fieldsCount = builderWrapper.getFieldsCount();
    builderWrapper.setPosition(0);

    for (int i = 0; i < fieldsCount; i++) {
      String line = reader.readLine();
      builderWrapper.setValue(line);
      builderWrapper.step();
    }
    return builderWrapper.build();
  }

  /**
   * Save collection in a file.
   *
   * @see Storage#saveCollection(AbstractMap.SimpleEntry)
   */
  @Override
  public void saveCollection(AbstractMap.SimpleEntry<LocalDateTime, Set<Model>> dateToCollection) throws StorageAccessException {
    try (FileWriter outputStream = new FileWriter(this.file)) {
      List<String[]> records = new ArrayList<>();
      records.add(new String[]{dateToCollection.getKey().toString()});
      records.addAll(dateToCollection.getValue().stream()
          .map(values -> values.getValuesRecursive().stream().map(e -> Objects.requireNonNullElse(e, "").toString()).toArray(String[]::new)).toList()
      );

      CSVPrinter printer = new CSVPrinter(outputStream, CSVFormat.INFORMIX_UNLOAD);
      printer.printRecords(records);
    } catch (IOException e) {
      throw new StorageAccessException("Failed to write collection to a file!");
    }
  }
}