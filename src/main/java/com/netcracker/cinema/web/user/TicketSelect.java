package com.netcracker.cinema.web.user;

import com.netcracker.cinema.model.Place;
import com.netcracker.cinema.model.Seance;
import com.netcracker.cinema.model.Zone;
import com.netcracker.cinema.service.PlaceService;
import com.netcracker.cinema.service.TicketService;
import com.netcracker.cinema.service.ZoneService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringComponent
@ViewScope
public class TicketSelect extends GridLayout {
    @Autowired
    private PlaceService placeService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private ZoneService zoneService;

    private Seance seance;
    private List<PlaceButton> placeButtons;
    private Set<PlaceSelectedListener> listeners;

    public void buildForThisSeance(Seance seance) {
        this.seance = seance;
        listeners = new HashSet<>();
        List<Place> places = placeService.getByHall(seance.getHallId());
        placeButtons = new ArrayList<>(places.size());
        adjustGridSize(places);
        for (Place place : places) {
            PlaceButton placeButton = new PlaceButton(place);
            placeButtons.add(placeButton);
            addComponent(placeButton, place.getNumber() - 1, place.getRowNumber() - 1);
        }
    }

    public List<Place> getSelectedPlaces() {
        List<Place> selectedPlaces = new ArrayList<>();
        for (PlaceButton placeButton : placeButtons) {
            if (placeButton.isSelected())
                selectedPlaces.add(placeButton.getPlace());
        }
        return selectedPlaces;
    }

    public void addPlaceSelectedListener(PlaceSelectedListener listener) {
        listeners.add(listener);
    }

    public void removePlaceSelectedListener(PlaceSelectedListener listener) {
        listeners.remove(listener);
    }

    private void adjustGridSize(List<Place> places) {
        int rows = 0, columns = 0;
        for (Place place : places) {
            if (place.getNumber() > columns) {
                columns = place.getNumber();
            }
            if (place.getRowNumber() > rows) {
                rows = place.getRowNumber();
            }
        }
        setColumns(columns);
        setRows(rows);
    }

    private class PlaceButton extends Button {
        private Place place;
        private boolean selected;

        public PlaceButton(Place place) {
            this.place = place;
            this.selected = false;

            Zone ticketZone = zoneService.getById(place.getZoneId());
            setCaption(ticketZone.getName() + " " + place.getRowNumber() + " " + place.getNumber());
            //todo: maybe better use own css styles
            setStyleName("primary");
            setWidth("100px");

            if (ticketService.isAlreadyBookedTicket(seance.getId(), place.getId())) {
                setEnabled(false);
            }

            addClickListener((ClickListener) event -> {
                selected = !selected;
                if (selected) {
                    setStyleName("danger");
                } else {
                    setStyleName("primary");
                }

                for(PlaceSelectedListener placeSelectedListener: listeners) {
                    placeSelectedListener.placeClicked(place, selected);
                }
            });
        }

        public Place getPlace() {
            return place;
        }

        public boolean isSelected() {
            return selected;
        }
    }

    public interface PlaceSelectedListener {
        void placeClicked(Place place, boolean selected);
    }
}