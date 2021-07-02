package com.jozsef.mongodb.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Builder
@Document("airports")
public class Airport {

    @Id
    private String id;
    private String name;
    private String city;
    private int passengersServed;

}
