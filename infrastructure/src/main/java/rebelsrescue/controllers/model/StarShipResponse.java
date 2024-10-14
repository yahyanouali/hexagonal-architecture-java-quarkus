package rebelsrescue.controllers.model;

import rebelsrescue.fleet.model.StarShip;

import java.util.Map;

public record StarShipResponse(String name, int capacity, int passengersCapacity, Map<String, String> deprecations) {
    public StarShipResponse(StarShip starShip) {
        this(
                starShip.name(),
                starShip.passengersCapacity(),
                starShip.passengersCapacity(),
                Map.of("capacity", "passengersCapacity"));
    }
}
