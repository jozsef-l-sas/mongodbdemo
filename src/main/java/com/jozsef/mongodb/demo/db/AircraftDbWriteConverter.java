package com.jozsef.mongodb.demo.db;

import com.jozsef.mongodb.demo.domain.Aircraft;
import org.springframework.core.convert.converter.Converter;

public class AircraftDbWriteConverter implements Converter<Aircraft, String> {

    @Override
    public String convert(Aircraft aircraft) {
        if (aircraft == null) {
            return null;
        }

        return aircraft.getModel() + "/" + aircraft.getNbSeats();
    }

}
