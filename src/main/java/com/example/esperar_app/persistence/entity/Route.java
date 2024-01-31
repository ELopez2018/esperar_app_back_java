package com.example.esperar_app.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "routes")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Route {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name = "from_place")
    private String from;

    @Column(name = "to_place")
    private String to;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name="deleted_at")
    private Date deletedAt;

    @OneToMany(mappedBy = "route", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Coordinate> coordinates;

}
