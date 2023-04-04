package com.driver.repository;

import com.driver.model.Station;
import com.driver.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Integer> {
//    @Query("SELECT t FROM Ticket t WHERE t.train.id = :trainId AND t.fromStation = :fromStation AND t.toStation = :toStation")
//    List<Ticket> findBookedTicketsByTrainAndStations(@Param("trainId") int trainId, @Param("fromStation") Station fromStation, @Param("toStation") Station toStation);



}



