package com.jozsef.mongodb.demo.db;

import com.jozsef.mongodb.demo.domain.FlightInformation;
import com.jozsef.mongodb.demo.domain.FlightType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightInformationRepository extends MongoRepository<FlightInformation, String> {

    List<FlightInformation> findByDestinationCity(String destination);

    List<FlightInformation> findByDurationMinLessThan(int duration);

    List<FlightInformation> findByDepartureCity(String departure);

    List<FlightInformation> findByDurationMinBetween(int start, int end);

    List<FlightInformation> findByAircraft_Model(String aircraftModel);

    List<FlightInformation> findByIsDelayedIsTrueAndDepartureCity(String departure);

    List<FlightInformation> findAllBy(TextCriteria criteria, Pageable pageable);

    List<FlightInformation> findByAircraft_NbSeatsGreaterThanEqual(int nbSeats);

    @Query("{'aircraft.nbSeats' : {$gte: ?0}}")
    List<FlightInformation> findByMinAircraftNbSeats(int nbSeats);

    Long countAllByType(FlightType type);

}
