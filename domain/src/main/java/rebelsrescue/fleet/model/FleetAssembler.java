package rebelsrescue.fleet.model;

import rebelsrescue.ddd.DomainService;
import rebelsrescue.fleet.api.AssembleAFleet;
import rebelsrescue.fleet.spi.FleetsRepository;
import rebelsrescue.fleet.spi.StarShipInventory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

@DomainService
public class FleetAssembler implements AssembleAFleet {
    private final StarShipInventory starshipsInventory;
    private final FleetsRepository fleetsRepository;
    public static final BigDecimal MINIMAL_CARGO_CAPACITY = new BigDecimal("100000");

    public FleetAssembler(StarShipInventory starShipsInventory, FleetsRepository fleetsRepository) {
        this.starshipsInventory = starShipsInventory;
        this.fleetsRepository = fleetsRepository;
    }

    @Override
    public Fleet forPassengers(int numberOfPassengers) {
        List<StarShip> starShips = getStarShipsHavingPassengersCapacity();
        List<StarShip> rescueStarShips = selectStarShips(numberOfPassengers, starShips);
        return fleetsRepository.save(new Fleet(rescueStarShips));
    }

    private List<StarShip> selectStarShips(int numberOfPassengers, List<StarShip> starShips) {
        starShips.removeIf(doesntHaveEnoughCargoCapacity());
        List<StarShip> rescueStarShips = new ArrayList<>();
        while (numberOfPassengers > 0) {
            var starShip = starShips.remove(0);
            numberOfPassengers -= starShip.passengersCapacity();
            rescueStarShips.add(starShip);
        }
        return rescueStarShips;
    }

    private Predicate<? super StarShip> doesntHaveEnoughCargoCapacity() {
        return starShip -> starShip.cargoCapacity().compareTo(MINIMAL_CARGO_CAPACITY) < 0;
    }

    private List<StarShip> getStarShipsHavingPassengersCapacity() {
        return starshipsInventory.starShips().stream()
                .filter(starShip -> starShip.passengersCapacity() > 0)
                .sorted(comparingInt(StarShip::passengersCapacity))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
