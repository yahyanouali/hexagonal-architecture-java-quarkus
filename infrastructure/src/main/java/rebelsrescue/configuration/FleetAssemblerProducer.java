package rebelsrescue.configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import rebelsrescue.fleet.model.FleetAssembler;
import rebelsrescue.fleet.spi.FleetsRepository;
import rebelsrescue.fleet.spi.StarShipInventory;

@ApplicationScoped
public class FleetAssemblerProducer {

    private final StarShipInventory starshipsInventory;
    private final FleetsRepository fleetsRepository;

    @Inject
    public FleetAssemblerProducer(StarShipInventory starShipsInventory, FleetsRepository fleetsRepository) {
        this.starshipsInventory = starShipsInventory;
        this.fleetsRepository = fleetsRepository;
    }

    @Produces
    public FleetAssembler createFleetAssembler() {
        return new FleetAssembler(starshipsInventory, fleetsRepository);
    }
}
