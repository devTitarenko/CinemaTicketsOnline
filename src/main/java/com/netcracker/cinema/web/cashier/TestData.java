package com.netcracker.cinema.web.cashier;

import com.netcracker.cinema.model.Ticket;
import com.netcracker.cinema.podam.CreatedWithStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dimka on 04.12.2016.
 */
public class TestData {

    CreatedWithStrategy createdWithStrategy = new CreatedWithStrategy();

    private List<Ticket> ticketList = new ArrayList<>();

    public TestData() {
        for(int i = 0; i < 50; i++) {
            ticketList.add(createdWithStrategy.getTicketPodam());
        }
    }

    public List<Ticket> getListTicket() {
        return ticketList;
    }

    public List<Ticket> getTicketListByCode(long code) {
        List<Ticket> tickets = new ArrayList<>();
        for (Ticket ticket: ticketList ) {
            if(ticket.getCode() == code) {
                tickets.add(ticket);
            }
        }
        return tickets;
    }

    public void save(Ticket ticket) {
        for(int i = 0; i < ticketList.size(); i++) {
            if(ticketList.get(i).getId() == ticket.getId()) {
                if(ticket.isPaid() == true) {
                    ticketList.set(i, ticket);
                } else {
                    ticketList.remove(i);
                }
            }
        }
    }

    public void delete(Ticket ticket) {
        for(int i = 0; i < ticketList.size(); i++) {
            if(ticketList.get(i).getId() == ticket.getId()) {
                ticketList.remove(i);
            }
        }
    }

}