package com.example.esperar_app.persistence.entity.coordinate;

import com.example.esperar_app.persistence.entity.route.Route;
import com.example.esperar_app.persistence.entity.station.Station;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "coordinates")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Coordinate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String latitude;

    @Column
    private String longitude;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name="deleted_at")
    private Date deletedAt;

    @ManyToOne
    @JoinColumn(name = "route_id")
    @JsonIgnore
    private Route route;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;
}
