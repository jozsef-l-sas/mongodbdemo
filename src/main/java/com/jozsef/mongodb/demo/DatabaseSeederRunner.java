package com.jozsef.mongodb.demo;

import com.jozsef.mongodb.demo.db.AirportRepository;
import com.jozsef.mongodb.demo.db.FlightInformationRepository;
import com.jozsef.mongodb.demo.domain.Aircraft;
import com.jozsef.mongodb.demo.domain.Airport;
import com.jozsef.mongodb.demo.domain.FlightInformation;
import com.jozsef.mongodb.demo.domain.FlightPrinter;
import com.jozsef.mongodb.demo.domain.FlightType;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Component
@Order(1)
@Log4j2
public class DatabaseSeederRunner implements CommandLineRunner {

    private FlightInformationRepository flightInformationRepository;
    private AirportRepository airportRepository;

    public DatabaseSeederRunner(FlightInformationRepository flightInformationRepository, AirportRepository airportRepository) {
        this.flightInformationRepository = flightInformationRepository;
        this.airportRepository = airportRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        empty();
        seed();
    }

    private void empty() {
        this.airportRepository.deleteAll();
        this.flightInformationRepository.deleteAll();
    }

    private void seed() {
        // airports
        Airport rome = Airport.builder().name("Leonardo da Vinci").city("Rome").passengersServed(42995119).build();
        Airport paris = Airport.builder().name("Charles de Gaulle").city("Paris").passengersServed(72229723).build();
        Airport copenhagen = Airport.builder().name("Copenhagen Airport").city("Copenhagen").passengersServed(30298531).build();
        Airport newYork = Airport.builder().name("New York Airport").city("New York").passengersServed(82229723).build();
        Airport bucharest = Airport.builder().name("Henri Coanda").city("Bucharest").passengersServed(32298531).build();
        Airport lasVegas = Airport.builder().name("Las Vegas Airport").city("Las Vegas").passengersServed(62229723).build();
        Airport washington = Airport.builder().name("Washington Airport").city("Washington").passengersServed(52229723).build();
        Airport brussels = Airport.builder().name("Brussels Airport").city("Brussels").passengersServed(30298531).build();
        Airport madrid = Airport.builder().name("Madrid Airport").city("Madrid").passengersServed(30298531).build();
        Airport barcelona = Airport.builder().name("Barcelona Airport").city("Barcelona").passengersServed(30298531).build();

        FlightInformation flight1 = FlightInformation.builder()
                .departure(rome)
                .destination(paris)
                .type(FlightType.INTERNATIONAL)
                .durationMin(80)
                .departureDate(LocalDate.now().plusDays(3))
                .aircraft(Aircraft.builder().model("737").nbSeats(180).build())
                .build();

        FlightInformation flight2 = FlightInformation.builder()
                .departure(newYork)
                .destination(copenhagen)
                .type(FlightType.INTERNATIONAL)
                .durationMin(600)
                .departureDate(LocalDate.now().plusDays(3))
                .aircraft(Aircraft.builder().model("747").nbSeats(300).build())
                .description("Flight via Rome")
                .build();

        FlightInformation flight3 = FlightInformation.builder()
                .departure(lasVegas)
                .destination(washington)
                .type(FlightType.INTERNAL)
                .durationMin(400)
                .departureDate(LocalDate.now().plusDays(3))
                .aircraft(Aircraft.builder().model("A319").nbSeats(150).build())
                .build();

        FlightInformation flight4 = FlightInformation.builder()
                .departure(bucharest)
                .destination(rome)
                .type(FlightType.INTERNATIONAL)
                .isDelayed(false)
                .durationMin(110)
                .departureDate(LocalDate.now().plusDays(3))
                .aircraft(Aircraft.builder().model("A321 Neo").nbSeats(200).build())
                .build();

        FlightInformation flight5 = FlightInformation.builder()
                .departure(madrid)
                .destination(barcelona)
                .type(FlightType.INTERNAL)
                .isDelayed(true)
                .durationMin(120)
                .departureDate(LocalDate.now().plusDays(3))
                .aircraft(Aircraft.builder().model("A319").nbSeats(150).build())
                .build();

        FlightInformation flight6 = FlightInformation.builder()
                .departure(brussels)
                .destination(bucharest)
                .type(FlightType.INTERNATIONAL)
                .isDelayed(true)
                .durationMin(150)
                .departureDate(LocalDate.now().plusDays(3))
                .aircraft(Aircraft.builder().model("A320").nbSeats(170).build())
                .build();

        this.flightInformationRepository.insert(Arrays.asList(flight1, flight2, flight3, flight4, flight5, flight6));

        long count = this.flightInformationRepository.count();
        log.info("Total flights in database: " + count);

        List<FlightInformation> flights = this.flightInformationRepository.findAll();
        flights.sort(Comparator.comparing(flightInformation -> flightInformation.getDeparture().getCity()));
        FlightPrinter.print(flights);

        log.info("--- Seeder finished ---\n");
    }

}
