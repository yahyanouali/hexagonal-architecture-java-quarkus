package rebelsrescue.fleet.spi.stubs;

import rebelsrescue.ddd.Stub;
import rebelsrescue.fleet.model.Fleet;
import rebelsrescue.fleet.spi.FleetsRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Stub
public class InMemoryFleetsRepository implements FleetsRepository {
    private final Map<UUID, Fleet> fleets = new HashMap<>();

    @Override
    public Fleet getById(UUID id) {
        return fleets.get(id);
    }

    @Override
    public Fleet save(Fleet fleet) {
        fleets.put(fleet.id(), fleet);
        return fleet;
    }
}
