package rebelsrescue.swapi;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import rebelsrescue.fleet.model.StarShip;
import rebelsrescue.fleet.spi.StarShipInventory;
import rebelsrescue.swapi.model.SwapiResponse;
import rebelsrescue.swapi.model.SwapiStarShip;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;

@ApplicationScoped
public class SwapiClient implements StarShipInventory {

    @ConfigProperty(name = "swapi.base-uri")
    String swapiBaseUri;

    private final List<String> invalidCapacitiesValues = asList("n/a", "unknown");

    @Inject
    @RestClient
    SwapiRestClient swapiRestClient;

    @Override
    public List<StarShip> starShips() {
        List<StarShip> starShips = new ArrayList<>();

        int page = 1;
        var swapiResponse = swapiRestClient.getStarShips(page);

        while (swapiResponse.next() != null) {
            starShips.addAll(convertSwapiResponseToStarShips(swapiResponse));
            swapiResponse = swapiRestClient.getStarShips(++page);
        }
        return starShips;
    }

    private List<StarShip> convertSwapiResponseToStarShips(SwapiResponse swapiResponse) {
        return swapiResponse.results().stream()
                .filter(hasValidPassengersValue())
                .map(toStarShip())
                .collect(Collectors.toList());
    }

    private Function<SwapiStarShip, StarShip> toStarShip() {
        return swapiStarShip ->
                new StarShip(
                        swapiStarShip.name(),
                        parseInt(swapiStarShip.passengers().replaceAll(",", "")),
                        new BigDecimal(swapiStarShip.cargoCapacity()));
    }

    private Predicate<SwapiStarShip> hasValidPassengersValue() {
        return swapiStarShip -> swapiStarShip.passengers() != null
                && !invalidCapacitiesValues.contains(swapiStarShip.passengers())
                && !invalidCapacitiesValues.contains(swapiStarShip.cargoCapacity());
    }
}

