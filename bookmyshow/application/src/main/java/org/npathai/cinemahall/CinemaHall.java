package org.npathai.cinemahall;

import org.npathai.cinemahall.auditorium.Auditorium;

import java.util.ArrayList;
import java.util.List;

public class CinemaHall {
    private long id;
    private String name;
    private Address address;
    private Rating rating;
    private List<Auditorium> audis = new ArrayList<>();

    public CinemaHall(long id, String name, Address address, Rating rating, List<Auditorium> audis) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.audis = audis;
    }

    public List<Auditorium> getAudis() {
        return audis;
    }
}
