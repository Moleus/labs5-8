package server.collection;

import lombok.extern.log4j.Log4j2;
import model.data.Flat;
import server.generated.repository.FlatRepository;

import java.time.LocalDate;

/**
 * Manager class which stores collection and provides an API to interact with it.
 */
@Log4j2
public class FlatsCollectionManager extends GenericCollectionManager<Flat> {
  public FlatsCollectionManager(FlatRepository flatRepository) {
    super(new FlatChangesTracker(), flatRepository);
  }

  @Override
  public long add(Flat entity) {
    entity.setCreationDate(LocalDate.now());
    entity.setUserId(1);
    long id = super.add(entity);
    entity.setId(id);
    return id;
  }
}