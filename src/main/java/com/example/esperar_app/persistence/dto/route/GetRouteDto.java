package com.example.esperar_app.persistence.dto.route;

import com.example.esperar_app.persistence.dto.coordinate.GetCoordinateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Data transfer object for getting a route
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GetRouteDto {
    /**
     * The id of the route
     */
    private Long id;

    /**
     * The name of the route
     */
    private String name;

    /**
     * The place where the route starts
     */
    private String from;

    /**
     * The place where the route ends
     */
    private String to;

    /**
     * The date and time when the route was created
     */
    private Date createdAt;

    /**
     * The date and time when the route was last updated
     */
    private Date updatedAt;

    /**
     * The date and time when the route was deleted
     */
    private Date deletedAt;

    /**
     * The list of coordinates of the route
     */
    private List<GetCoordinateDto> coordinates;
}
