package server.collection;

import lombok.extern.log4j.Log4j2;
import model.data.Flat;
import perform.database.repository.FlatRepository;

/**
 * Manager class which stores collection and provides an API to interact with it.
 */
@Log4j2
public class FlatsCollectionManager extends GenericCollectionManager<Flat> {
  //  Set s = Collections.synchronizedSet(new LinkedHashSet(...));

  private FlatRepository flatRepository;

  public FlatsCollectionManager(FlatRepository flatRepository) {
    this.flatRepository = flatRepository;
  }
}