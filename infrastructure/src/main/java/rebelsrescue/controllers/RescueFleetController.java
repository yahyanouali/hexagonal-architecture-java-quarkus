package rebelsrescue.controllers;

import rebelsrescue.controllers.model.FleetResponse;
import rebelsrescue.controllers.model.RescueFleetRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import rebelsrescue.fleet.api.AssembleAFleet;
import rebelsrescue.fleet.spi.FleetsRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;


@Path("/rescueFleets")
public class RescueFleetController {
    private AssembleAFleet assembleAFleet;
    private FleetsRepository fleetsRepository;

    public RescueFleetController(AssembleAFleet assembleAFleet, FleetsRepository fleetsRepository) {
        this.assembleAFleet = assembleAFleet;
        this.fleetsRepository = fleetsRepository;
    }

    @POST
    @Path("/assemble")
    public Response assembleAFleet(RescueFleetRequest rescueFleetRequest) throws URISyntaxException {
        var fleet = assembleAFleet.forPassengers(rescueFleetRequest.numberOfPassengers);

        // Create the URI for the newly created resource
        URI uri = UriBuilder.fromResource(RescueFleetController.class)
                .path(RescueFleetController.class, "getFleetById")
                .build(fleet.id());

        return Response.created(uri)
                .entity(new FleetResponse(fleet))
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getFleetById(@PathParam("id") UUID id) {
        return Response.ok(new FleetResponse(fleetsRepository.getById(id)))
                .build();
    }

}
