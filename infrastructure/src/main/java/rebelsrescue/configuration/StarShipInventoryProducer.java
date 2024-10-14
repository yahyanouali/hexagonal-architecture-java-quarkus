package rebelsrescue.configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import rebelsrescue.fleet.spi.StarShipInventory;
import rebelsrescue.fleet.spi.stubs.StarShipInventoryStub;

@ApplicationScoped
public class StarShipInventoryProducer {

    @Produces
    StarShipInventory createStarShipInventory() {
        return new StarShipInventoryStub();
    }
}
