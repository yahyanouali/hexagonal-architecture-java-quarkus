package fleet;

import rebelsrescue.fleet.model.Fleet;
import rebelsrescue.fleet.model.FleetAssembler;
import rebelsrescue.fleet.model.StarShip;
import rebelsrescue.fleet.api.AssembleAFleet;
import rebelsrescue.fleet.spi.FleetsRepository;
import rebelsrescue.fleet.spi.StarShipInventory;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import rebelsrescue.fleet.spi.stubs.InMemoryFleetsRepository;
import rebelsrescue.fleet.spi.stubs.StarShipInventoryStub;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

import static rebelsrescue.fleet.model.FleetAssembler.MINIMAL_CARGO_CAPACITY;
import static java.math.BigDecimal.ZERO;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class AssembleAFleetFunctionalTest {

    @Test
    void should_assemble_a_fleet_for_1050_passengers() {
        //Given
        var starShips = asList(
                new StarShip("no-passenger-ship", 0, ZERO),
                new StarShip("xs", 10, new BigDecimal("1000")),
                new StarShip("s", 50, new BigDecimal("50000")),
                new StarShip("m", 200, new BigDecimal("70000")),
                new StarShip("l", 800, new BigDecimal("150000")),
                new StarShip("xl", 2000, new BigDecimal("500000")));
        var numberOfPassengers = 1050;

        StarShipInventory starShipsInventory = new StarShipInventoryStub(starShips);
        FleetsRepository fleetsRepository = new InMemoryFleetsRepository();
        AssembleAFleet assembleAFleet = new FleetAssembler(starShipsInventory, fleetsRepository);

        //When
        Fleet fleet = assembleAFleet.forPassengers(numberOfPassengers);

        //Then
        System.out.println(fleet);
        assertThat(fleet.starships())
                .has(enoughCapacityForThePassengers(numberOfPassengers))
                .allMatch(hasPassengersCapacity())
                .allMatch(hasEnoughCargoCapacity(), "hasEnoughCargoCapacity");

        assertThat(fleetsRepository.getById(fleet.id())).isEqualTo(fleet);
    }

    private Predicate<? super StarShip> hasPassengersCapacity() {
        return starShip -> starShip.passengersCapacity() > 0;
    }

    private Predicate<? super StarShip> hasEnoughCargoCapacity() {
        return starShip -> starShip.cargoCapacity().compareTo(MINIMAL_CARGO_CAPACITY) >= 0;
    }

    private Condition<? super List<? extends StarShip>> enoughCapacityForThePassengers(int numberOfPassengers) {
        return new Condition<>(
                starShips ->
                        starShips.stream()
                                .map(StarShip::passengersCapacity)
                                .reduce(0, Integer::sum) >= numberOfPassengers,
                "passengersCapacity check");
    }

}
