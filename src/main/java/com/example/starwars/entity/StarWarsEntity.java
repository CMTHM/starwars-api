package com.example.starwars.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StarWarsEntity {
    private String name;
    private String type; // Planet, Spaceship, Vehicle, Person, Film, Species


}
