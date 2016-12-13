package com.netcracker.cinema.dao;

import com.netcracker.cinema.model.Ticket;

import java.sql.Date;
import java.util.List;

/**
 * Created by Titarenko on 01.12.2016.
 */
public interface TicketDao {

    List<Ticket> findAll();

    List<Ticket> getTicketsByCode(long code);

    List<Ticket> getBySeanceOrPlace(long id);

    Ticket get(long ticketId);

    void delete(long ticketId);

    long save(Ticket ticket);

    int soldTickets(long objId, Date startDate, Date endDate);
}