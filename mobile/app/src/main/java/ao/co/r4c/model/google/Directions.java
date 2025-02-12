package ao.co.r4c.model.google;

import java.util.List;

import lombok.Data;

/**
 * Created by abhishek
 * on 04/11/17.
 */

@Data
public class Directions {
    private String status;
    private List<GeocodedWayPoints> geocodedWaypoints;
    private List<Routes> routes;
}
