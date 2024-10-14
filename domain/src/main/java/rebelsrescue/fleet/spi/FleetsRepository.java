package rebelsrescue.fleet.spi;

import rebelsrescue.fleet.model.Fleet;

import java.util.UUID;

public interface FleetsRepository {
    Fleet getById(UUID id);

    Fleet save(Fleet fleet);
}
