package app.storage;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import app.collection.FieldsInputMode;
import app.collection.FieldsReader;
import app.collection.FlatBuilder;
import app.exceptions.CollectionCorruptedException;
import app.exceptions.InvalidDataValues;
import app.exceptions.ReadFailedException;
import app.exceptions.StorageAccessException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import app.collection.data.Flat;

import javax.swing.text.html.Option;

/**
 * Provides API to load or save a collection from/to file.
 */
public class FileStorage implements Storage {
  private final File file;
  private final FlatBuilder flatBuilder;

  public FileStorage(String filePath, FlatBuilder flatBuilder) {
    this.file = new File(filePath);
    this.flatBuilder = flatBuilder;
  }

  /**
   * Load collection from a file.
   * @see Storage#loadCollection()
   */
  @Override
  public LinkedHashSet<Flat> loadCollection() throws CollectionCorruptedException, StorageAccessException {
    final int MAX_DATA_LENGTH = 1000;
    LinkedHashSet<Flat> collection = new LinkedHashSet<>();
    try (FileInputStream inputStream = new FileInputStream(file)) {
      InputStreamReader reader = new InputStreamReader(new BufferedInputStream(inputStream));
      String data = readInput(reader, MAX_DATA_LENGTH).trim();
      if (data.length() == MAX_DATA_LENGTH) {
        throw new CollectionCorruptedException("File is too large.");
      }
      if (data.isEmpty()) {
        throw new CollectionCorruptedException("File is empty.");
      }
      CSVParser parser = CSVParser.parse(data, CSVFormat.INFORMIX_UNLOAD);
      FieldsReader fieldsReader = new FieldsReader(Flat.class);
      for (CSVRecord flat : parser) {
        String oneItem = flat.stream().collect(Collectors.joining(System.lineSeparator()));

        // Validate all values in flat
        BufferedReader bfReader = new BufferedReader(new StringReader(oneItem));
        Object[] flatValues = fieldsReader.read(bfReader, FieldsInputMode.STORAGE);
        Flat newFlat = flatBuilder.buildAll(flatValues);
        if (!collection.add(newFlat)) {
          // contains duplicate id
          throw new CollectionCorruptedException("Collection contains duplicating ids");
        }
      }
    } catch (FileNotFoundException e) {
      throw new StorageAccessException("Can't find a file to load collection");
    } catch (IOException e) {
      System.out.printf("Failed to read collection from file. IO error: %s%n", e.getMessage());
    } catch (SecurityException e) {
      throw new StorageAccessException("Permissions denied while loading collection. Check file read permissions");
    } catch (ReadFailedException | InvalidDataValues e) {
      throw new CollectionCorruptedException("File is corrupted. Error: " + e.getMessage());
    }
    return collection;
  }

  private String readInput(InputStreamReader reader, int length) throws IOException {
    char[] chars = new char[length];
    if (reader.read(chars, 0, length) == -1) return "";
    return String.valueOf(chars);
  }

  /**
   * Save collection in a file.
   * @see Storage#saveCollection(LinkedHashSet)
   */
  @Override
  public void saveCollection(LinkedHashSet<Flat> collection) throws StorageAccessException {
    try (FileWriter outputStream = new FileWriter(this.file)) {
      List<String[]> records = collection.stream()
          .map(values -> values.getValuesRecursive().stream().map(e -> e==null?"":e.toString()).toArray(String[]::new)).collect(Collectors.toList());
      CSVPrinter printer = new CSVPrinter(outputStream, CSVFormat.INFORMIX_UNLOAD);
      printer.printRecords(records);
    } catch (IOException e) {
      throw new StorageAccessException("Failed to write collection to a file!");
    }
  }
}