package com.jozsef.mongodb.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Aircraft {

    private String model;
    private int nbSeats;

}
