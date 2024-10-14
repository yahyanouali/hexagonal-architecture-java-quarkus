package rebelsrescue.controllers.model;

import rebelsrescue.fleet.model.Fleet;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public record FleetResponse(UUID id, List<StarShipResponse> starships) {
    public FleetResponse(Fleet fleet) {
        this(fleet.id(), fleet.starships().stream().map(StarShipResponse::new).collect(toList()));
    }
}
