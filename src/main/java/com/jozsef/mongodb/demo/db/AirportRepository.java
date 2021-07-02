package com.jozsef.mongodb.demo.db;

import com.jozsef.mongodb.demo.domain.Airport;
import com.jozsef.mongodb.demo.domain.FlightInformation;
import com.jozsef.mongodb.demo.domain.FlightType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends MongoRepository<Airport, String> {

    Airport findByCity(String city);

}
