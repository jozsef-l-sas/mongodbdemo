package com.jozsef.mongodb.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.jozsef.mongodb.demo.db.AirportRepository;
import com.jozsef.mongodb.demo.db.FlightInformationRepository;
import com.jozsef.mongodb.demo.domain.Aircraft;
import com.jozsef.mongodb.demo.domain.Airport;
import com.jozsef.mongodb.demo.domain.FlightInformation;
import com.jozsef.mongodb.demo.domain.FlightType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Import(DatabaseTestConfiguration.class)
public class DatabaseIntegrationTests {

    private static final String TEST_ID = "ad7dc654-4c00-4623-9d7c-df702d471507";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private FlightInformationRepository flightInformationRepository;

    @BeforeEach
    public void beforeEach() {
        Airport rome = Airport.builder().name("Leonardo da Vinci").city("Rome").passengersServed(42995119).build();
        Airport paris = Airport.builder().name("Charles de Gaulle").city("Paris").passengersServed(72229723).build();
        Airport copenhagen = Airport.builder().name("Copenhagen Airport").city("Copenhagen").passengersServed(30298531).build();

        this.airportRepository.saveAll(Arrays.asList(rome, paris, copenhagen));

        FlightInformation flight1 = FlightInformation.builder()
                .departure(rome)
                .destination(paris)
                .type(FlightType.INTERNATIONAL)
                .durationMin(80)
                .departureDate(LocalDate.now().plusDays(3))
                .aircraft(Aircraft.builder().model("737").nbSeats(180).build())
                .build();

        FlightInformation flight2 = FlightInformation.builder()
                .id(TEST_ID)
                .departure(paris)
                .destination(copenhagen)
                .type(FlightType.INTERNATIONAL)
                .durationMin(600)
                .departureDate(LocalDate.now().plusDays(3))
                .aircraft(Aircraft.builder().model("747").nbSeats(300).build())
                .description("Flight via Rome")
                .build();

        this.flightInformationRepository.saveAll(Arrays.asList(flight1, flight2));
    }

    @AfterEach
    public void afterEach() {
        this.mongoTemplate.dropCollection(Airport.class);
        this.mongoTemplate.dropCollection(FlightInformation.class);
    }

    @Test
    public void findByMinAircraftNbSeatsShouldWork() {
        // arrange
        int minSeats = 200;

        // act
        List<FlightInformation> flights = this.flightInformationRepository.findByMinAircraftNbSeats(minSeats);

        // assert
        assertEquals(1, flights.size());
        assertEquals(TEST_ID, flights.get(0).getId());
    }

    @Test
    public void theCascadeOnSaveShouldWork() {
        // arrange
        Airport rome = Airport.builder().id("1111").name("Leonardo da Vinci").city("Rome").passengersServed(42995119).build();
        Airport paris = Airport.builder().id("1112").name("Charles de Gaulle").city("Paris").passengersServed(72229723).build();

        FlightInformation flight = FlightInformation.builder()
                .id("2222")
                .departure(rome)
                .destination(paris)
                .type(FlightType.INTERNATIONAL)
                .durationMin(80)
                .departureDate(LocalDate.now().plusDays(3))
                .aircraft(Aircraft.builder().model("737").nbSeats(180).build())
                .build();

        // act
        this.flightInformationRepository.insert(flight);

        // assert
        FlightInformation dbFlight = this.flightInformationRepository.findById("2222").get();
        assertNotNull(dbFlight);

        Airport dbRome = this.airportRepository.findById("1111").get();
        assertNotNull(dbRome);

        Airport dbParis = this.airportRepository.findById("1112").get();
        assertNotNull(dbParis);
    }

}
