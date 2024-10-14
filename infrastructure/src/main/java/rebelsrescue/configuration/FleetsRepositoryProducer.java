package rebelsrescue.configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import rebelsrescue.fleet.spi.FleetsRepository;
import rebelsrescue.fleet.spi.stubs.InMemoryFleetsRepository;

@ApplicationScoped
public class FleetsRepositoryProducer {

    @Produces
    FleetsRepository createFleets() {
        return new InMemoryFleetsRepository();
    }
}
