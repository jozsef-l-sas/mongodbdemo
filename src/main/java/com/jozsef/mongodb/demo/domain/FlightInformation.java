package com.jozsef.mongodb.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@Document("flights")
public class FlightInformation {

    @Id
    private String id;

    @DBRef
    private Airport departure;

    @DBRef
    private Airport destination;

    private FlightType type;
    private boolean isDelayed;
    private int durationMin;
    private LocalDate departureDate;
    private Aircraft aircraft;

    @TextIndexed(weight = 2)
    private String description;

    @Transient
    private LocalDate createdAt;

    public FlightInformation() {
        this.createdAt = LocalDate.now();
    }

}
