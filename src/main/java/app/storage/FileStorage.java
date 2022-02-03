package app.storage;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.stream.Collectors;

import app.collection.FieldsInputMode;
import app.collection.FieldsReader;
import app.collection.FlatBuilder;
import app.exceptions.CollectionCorruptedException;
import app.exceptions.ReadFailedException;
import app.exceptions.StorageAccessException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import app.collection.data.Flat;

public class FileStorage implements Storage {
  private final File file;

  public FileStorage(String filePath) {
    this.file = new File(filePath);
  }

  @Override
  public LinkedHashSet<Flat> loadCollection() throws CollectionCorruptedException, StorageAccessException {
    LinkedHashSet<Flat> collection = new LinkedHashSet<>();
    try (FileInputStream inputStream = new FileInputStream(file)) {
      BufferedInputStream bInpStream = new BufferedInputStream(inputStream);
      CSVFormat format = CSVFormat.Builder.create().setAllowDuplicateHeaderNames(false).build();
      CSVParser parser = CSVParser.parse(new InputStreamReader(bInpStream), format);
      FieldsReader fieldsReader = new FieldsReader(Flat.class);
      for (CSVRecord flat : parser) {
        String oneItem = flat.stream().collect(Collectors.joining("\n"));

        // Validate all values in flat
        BufferedReader bfReader = new BufferedReader(new StringReader(oneItem));
        Object[] flatValues = fieldsReader.read(bfReader, FieldsInputMode.STORAGE);
        Flat newFlat = FlatBuilder.getInstance().buildAll(flatValues);
        if (!collection.add(newFlat)) {
          // contains duplicate id
          throw new CollectionCorruptedException("Collection contains duplicating ids");
        }
      }
    } catch (FileNotFoundException e) {
      throw new StorageAccessException("Can't find a file to load collection");
    } catch (IOException e) {
      // FIXME: throw different message when understand why it's happening
      System.out.println("IO exception occurred on collection loading");
    } catch (SecurityException e) {
      throw new StorageAccessException("Permissions denied while loading collection. Check file read permissions");
    } catch (ReadFailedException e) {
      throw new CollectionCorruptedException("File is corrupted. Error: " + e.getMessage());
    }
    return collection;
  }

  @Override
  public void saveCollection(LinkedHashSet<Flat> collection) throws StorageAccessException {
    try (FileWriter outputStream = new FileWriter(this.file)) {
      CSVRecord csvRecord = CSVParser.parse(collection.stream()
          .map(values -> values.getValuesRecursive().stream().map(Objects::toString).collect(Collectors.joining(",")))
          .collect(Collectors.joining("\n")), CSVFormat.DEFAULT
      ).getRecords().get(0);
      CSVPrinter printer = new CSVPrinter(outputStream, CSVFormat.DEFAULT);
      printer.print(csvRecord);
    } catch (IOException e) {
      throw new StorageAccessException("Failed to write collection to a file!");
    }
  }
}
