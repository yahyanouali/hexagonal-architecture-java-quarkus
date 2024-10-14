package rebelsrescue.swapi;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import rebelsrescue.swapi.model.SwapiResponse;


@Path("/api/starships")
@RegisterRestClient(configKey = "swapi-api")
public interface SwapiRestClient {

    @GET
    SwapiResponse getStarShips(@QueryParam("page") int pageNumber);
}
