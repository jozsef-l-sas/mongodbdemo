package com.jozsef.mongodb.demo;

import com.jozsef.mongodb.demo.db.AirportRepository;
import com.jozsef.mongodb.demo.db.FlightInformationRepository;
import com.jozsef.mongodb.demo.domain.Airport;
import com.jozsef.mongodb.demo.domain.FlightInformation;
import com.jozsef.mongodb.demo.domain.FlightPrinter;
import com.jozsef.mongodb.demo.domain.FlightType;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Order(2)
@Log4j2
public class ApplicationRunner implements CommandLineRunner {

    private FlightInformationRepository flightInformationRepository;
    private AirportRepository airportRepository;

    public ApplicationRunner(FlightInformationRepository flightInformationRepository, AirportRepository airportRepository) {
        this.flightInformationRepository = flightInformationRepository;
        this.airportRepository = airportRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        runTestQueries();
//
//        markAllFlightsToRomeAsDelayed();
//        removeFlightsWithDurationLessThanTwoHours();

        testUpdateDbRef();

        log.info("Application started...");
    }

    private void markAllFlightsToRomeAsDelayed() {
        List<FlightInformation> allFlightsToRome = this.flightInformationRepository.findByDestinationCity("Rome");
        allFlightsToRome.forEach(f -> f.setDelayed(true));
        this.flightInformationRepository.saveAll(allFlightsToRome);
    }

    private void removeFlightsWithDurationLessThanTwoHours() {
        List<FlightInformation> lessThanTwoHoursFlight = this.flightInformationRepository.findByDurationMinLessThan(120);
        this.flightInformationRepository.deleteAll(lessThanTwoHoursFlight);
    }

    private void runTestQueries() {
        log.info("---\nQUERY: All flights ordered by departure");
        Page<FlightInformation> allFlightsOrdered = this.flightInformationRepository.findAll(PageRequest.of(0, 3, Sort.by("departure").ascending()));
        FlightPrinter.print(allFlightsOrdered.getContent());

        log.info("---\nQUERY: Depart at New York");
        List<FlightInformation> newYorkDepartures = this.flightInformationRepository.findByDepartureCity("New York");
        FlightPrinter.print(newYorkDepartures);

        log.info("---\nQUERY: Delayed at Bucharest");
        List<FlightInformation> delayedAtBucharest = this.flightInformationRepository.findByIsDelayedIsTrueAndDepartureCity("Bucharest");
        FlightPrinter.print(delayedAtBucharest);

        log.info("---\nQUERY: Duration between 60 and 120 minutes");
        List<FlightInformation> durationBetween = this.flightInformationRepository.findByDurationMinBetween(60, 120);
        FlightPrinter.print(durationBetween);

        log.info("---\nQUERY: Using a 737 aircraft");
        List<FlightInformation> flightsWith737 = this.flightInformationRepository.findByAircraft_Model("737");
        FlightPrinter.print(flightsWith737);

        log.info("---\nQUERY: Free text search 'Rome'");
        List<FlightInformation> flightByFreeText = this.flightInformationRepository.findAllBy(TextCriteria.forDefaultLanguage().matching("Rome"), PageRequest.of(0, 3, Sort.by("score")));
        FlightPrinter.print(flightByFreeText);

        log.info("---\nQUERY: Using an aircraft with more than 200 seats");
        List<FlightInformation> flightsWithMoreThan200Seats = this.flightInformationRepository.findByAircraft_NbSeatsGreaterThanEqual(200);
        FlightPrinter.print(flightsWithMoreThan200Seats);

        log.info("---\nQUERY: Using an aircraft with more than 200 seats NATIVE QUERY");
        List<FlightInformation> flightsWithMoreThan200SeatsNative = this.flightInformationRepository.findByMinAircraftNbSeats(200);
        FlightPrinter.print(flightsWithMoreThan200SeatsNative);

        log.info("Number of international flights: " + this.flightInformationRepository.countAllByType(FlightType.INTERNATIONAL));
    }

    private void testUpdateDbRef() {
        Airport rome = this.airportRepository.findByCity("Rome");
        rome.setName("Leonardo da Vinci (Fiumicino)");
        this.airportRepository.save(rome);

        log.info("AFTER UPDATE");
        List<FlightInformation> flights = this.flightInformationRepository.findAll();
        flights.sort(Comparator.comparing(flightInformation -> flightInformation.getDeparture().getCity()));
        FlightPrinter.print(flights);
    }

}
