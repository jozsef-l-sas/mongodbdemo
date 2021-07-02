package com.jozsef.mongodb.demo.db;

import com.jozsef.mongodb.demo.domain.Aircraft;
import org.springframework.core.convert.converter.Converter;

public class AircraftDbReadConverter implements Converter<String, Aircraft> {

    @Override
    public Aircraft convert(String s) {
        if (s == null) {
            return null;
        }

        String[] parts = s.split("/");
        Aircraft aircraft = Aircraft.builder().model(parts[0]).nbSeats(Integer.parseInt(parts[1])).build();

        return aircraft;
    }

}
