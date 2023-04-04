package com.driver.services;

import com.driver.EntryDto.AddTrainEntryDto;
import com.driver.EntryDto.SeatAvailabilityEntryDto;
import com.driver.model.Passenger;
import com.driver.model.Station;
import com.driver.model.Ticket;
import com.driver.model.Train;
import com.driver.repository.TicketRepository;
import com.driver.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainService {

    @Autowired
    TrainRepository trainRepository;

    @Autowired
    TicketRepository ticketRepository;


    public Integer addTrain(AddTrainEntryDto trainEntryDto){

        //Add the train to the trainRepository
        //and route String logic to be taken from the Problem statement.
        //Save the train and return the trainId that is generated from the database.
        //Avoid using the lombok library

        //Create a new Train object and set its properties using the data from the DTO

        Train train = new Train();
        train.setDepartureTime(trainEntryDto.getDepartureTime());
        train.setNoOfSeats(trainEntryDto.getNoOfSeats());

        //Construct the route String by concatenating the names of the stations in the stationRoute List
        List<Station> stationRoute = trainEntryDto.getStationRoute();


        StringBuilder routeBuilder = new StringBuilder();
        for(int i=0;i<stationRoute.size();i++){
            //here this condition is just for separating the station names using "->
            Station station = stationRoute.get(i);
            if(i>0) routeBuilder.append("->");
            routeBuilder.append(station.name());
        }
        String route = routeBuilder.toString();

        train.setRoute(route);
        //Save the Train object to the database using the TrainRepository

        Train savedTrain = trainRepository.save(train);

        //Return the ID of the saved Train object
        return savedTrain.getTrainId();
    }


    public Integer calculateAvailableSeats(SeatAvailabilityEntryDto seatAvailabilityEntryDto) {

        //Calculate the total seats available
        //Suppose the route is A B C D
        //And there are 2 seats available in total in the train
        //and 2 tickets are booked from A to C and B to D.
        //The seat is available only between A to C and A to B. If a seat is empty between 2 station it will be counted to our final ans
        //even if that seat is booked post the destStation or before the boardingStation
        //In short : a train has totalNo of seats and there are tickets from and to different locations
        //We need to find out the available seats between the given 2 stations.


            Train train = trainRepository.findById(seatAvailabilityEntryDto.getTrainId()).get();
            Station boardingStation = seatAvailabilityEntryDto.getFromStation();
            Station destStation = seatAvailabilityEntryDto.getToStation();
        // Calculate the total seats available
            int totalSeats = train.getNoOfSeats();
            int bookedSeats = 0;
// Iterate over all booked tickets to determine the available seats
            for (Ticket ticket : train.getBookedTickets()) {
                List<Passenger> passengers = ticket.getPassengersList();
                Station fromStation = ticket.getFromStation();
                Station toStation = ticket.getToStation();

                // check if the journey overlaps with the requested journey
                if ((boardingStation.compareTo(toStation) < 0 && destStation.compareTo(fromStation) > 0)
                        || (boardingStation.compareTo(fromStation) >= 0 && destStation.compareTo(toStation) <= 0)
                        || (boardingStation.compareTo(fromStation) < 0 && destStation.compareTo(toStation) > 0)) {
                    bookedSeats += passengers.size();
                }
            }

            int availableSeats = totalSeats - bookedSeats;
            return availableSeats;
        }

//        int trainId = seatAvailabilityEntryDto.getTrainId();
//        Station fromStation = seatAvailabilityEntryDto.getFromStation();
//        Station toStation = seatAvailabilityEntryDto.getToStation();
//
//
//        Train train = trainRepository.findById(seatAvailabilityEntryDto.getTrainId()).get();
//
//        int totalSeats = train.getNoOfSeats();

//        List<Ticket> bookedTickets = ticketRepository.findBookedTicketsByTrainAndStations(trainId,fromStation,toStation);
//        int bookedSeats =0;
//
//        for(Ticket ticket : bookedTickets) {
//


        // Get the train and its route based on the input DTO
//        Train train = trainRepository.findByRoute(seatAvailabilityEntryDto.getRoute());
//
//        // Get the stations for the from and to locations
//        Station fromStation = stationRepository.findByName(seatAvailabilityEntryDto.getFromStation());
//        Station toStation = stationRepository.findByName(seatAvailabilityEntryDto.getToStation());
//
//        // Calculate the total number of seats on the train
//        int totalSeats = train.getNoOfSeats();
//
//        // Loop through all the booked tickets for the train
//        for (Ticket ticket : train.getBookedTickets()) {
//            // Check if the ticket overlaps with the requested journey
//            if (ticket.getFromStation().equals(fromStation) || ticket.getToStation().equals(toStation) ||
//                    (ticket.getFromStation().compareTo(fromStation) > 0 && ticket.getToStation().compareTo(toStation) < 0)) {
//                // If so, subtract the number of passengers on the ticket from the total number of seats
//                totalSeats -= ticket.getPassengersList().size();
//            }
//        }
//
//        // Return the total number of available seats
//        return totalSeats;
//    }
    public Integer calculatePeopleBoardingAtAStation(Integer trainId,Station station) throws Exception{

        //We need to find out the number of people who will be boarding a train from a particular station
        //if the trainId is not passing through that station
        //throw new Exception("Train is not passing from this station");
        //  in a happy case we need to find out the number of such people.

        // Get the train object by its ID
        Train train = trainRepository.findById(trainId).get();

        // check if the train passes through the given station
        if(!train.getRoute().contains(station.name())){
            throw new Exception("Train is not passing from this station");
        }

        // calculate the number of people who will board the train at the given station
         int numberOfPeopleBoarding = 0;

        for(Ticket ticket: train.getBookedTickets()){
            if(ticket.getFromStation()==station){
                numberOfPeopleBoarding++;
            }
        }

        return numberOfPeopleBoarding;
    }

    public Integer calculateOldestPersonTravelling(Integer trainId) {

        //Throughout the journey of the train between any 2 stations
        //We need to find out the age of the oldest person that is travelling the train
        //If there are no people travelling in that train you can return 0

//        Train train = trainRepository.findById(trainId).get();
//        if (train == null) {
//            throw new IllegalArgumentException("Invalid train ID");
//        }
//        List<Ticket> tickets = train.getBookedTickets();
//        if(tickets== null || tickets.isEmpty()){
//            return 0; // No passengers on the train
//        }
//        Integer oldestAge = tickets.get(0).getPassengersList().get();
//
//
//        return
//    }
        int oldestAge = 0;

        // Find the train with the given ID
        Train train = trainRepository.findById(trainId).get();

        // If the train is null, return 0
        if (train == null) {
           throw new IllegalArgumentException("Invalid train ID");
      }

        // Loop through all the tickets booked for the train
        for (Ticket ticket : train.getBookedTickets()) {

            // Loop through all the passengers on the ticket
            for (Passenger passenger : ticket.getPassengersList()) {

                // Check if the passenger is older than the current oldestAge
                if (passenger.getAge() > oldestAge) {
                    oldestAge = passenger.getAge();
                }
            }
        }

        // Return the oldestAge
        return oldestAge;
    }

    public List<Integer> trainsBetweenAGivenTime(Station station, LocalTime startTime, LocalTime endTime){

        //When you are at a particular station you need to find out the number of trains that will pass through a given station
        //between a particular time frame both start time and end time included.
        //You can assume that the date change doesn't need to be done ie the travel will certainly happen with the same date (More details
        //in problem statement)
        //You can also assume the seconds and milli seconds value will be 0 in a LocalTime format.

        return null;
    }

}




//package com.driver.services;
//
//        import com.driver.EntryDto.AddTrainEntryDto;
//        import com.driver.EntryDto.SeatAvailabilityEntryDto;
//        import com.driver.model.Passenger;
//        import com.driver.model.Station;
//        import com.driver.model.Ticket;
//        import com.driver.model.Train;
//        import com.driver.repository.TrainRepository;
//        import org.springframework.beans.factory.annotation.Autowired;
//        import org.springframework.stereotype.Service;
//
//        import java.time.LocalTime;
//        import java.util.ArrayList;
//        import java.util.List;

//@Service
//public class TrainService {
//
//    @Autowired
//    TrainRepository trainRepository;
//
//    public Integer addTrain(AddTrainEntryDto trainEntryDto){
//
//        //Add the train to the trainRepository
//        //and route String logic to be taken from the Problem statement.
//        //Save the train and return the trainId that is generated from the database.
//        //Avoid using the lombok library
//
//
//        return null;
//    }
//
//    public Integer calculateAvailableSeats(SeatAvailabilityEntryDto seatAvailabilityEntryDto){
//
//        //Calculate the total seats available
//        //Suppose the route is A B C D
//        //And there are 2 seats avaialble in total in the train
//        //and 2 tickets are booked from A to C and B to D.
//        //The seat is available only between A to C and A to B. If a seat is empty between 2 station it will be counted to our final ans
//        //even if that seat is booked post the destStation or before the boardingStation
//        //Inshort : a train has totalNo of seats and there are tickets from and to different locations
//        //We need to find out the available seats between the given 2 stations.
//
//        return null;
//    }
//
//    public Integer calculatePeopleBoardingAtAStation(Integer trainId,Station station) throws Exception{
//
//        //We need to find out the number of people who will be boarding a train from a particular station
//        //if the trainId is not passing through that station
//        //throw new Exception("Train is not passing from this station");
//        //  in a happy case we need to find out the number of such people.
//
//
//        return 0;
//    }
//
//    public Integer calculateOldestPersonTravelling(Integer trainId){
//
//        //Throughout the journey of the train between any 2 stations
//        //We need to find out the age of the oldest person that is travelling the train
//        //If there are no people travelling in that train you can return 0
//
//        return 0;
//    }
//
//    public List<Integer> trainsBetweenAGivenTime(Station station, LocalTime startTime, LocalTime endTime){
//
//        //When you are at a particular station you need to find out the number of trains that will pass through a given station
//        //between a particular time frame both start time and end time included.
//        //You can assume that the date change doesn't need to be done ie the travel will certainly happen with the same date (More details
//        //in problem statement)
//        //You can also assume the seconds and milli seconds value will be 0 in a LocalTime format.
//
//        return null;
//    }
//
//}
