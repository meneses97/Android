package com.example.meneses.entities;


import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class UserLocation {

    private LatLng geoPoint;

    private Date timestamp;
    private User user;
    private Car car;
    private Rota rota;

    public UserLocation() {
    }

    public UserLocation(LatLng geoPoint, Date timestamp, User user) {
        this.geoPoint = geoPoint;
        this.timestamp = timestamp;
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LatLng getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(LatLng geoPoint) {
        this.geoPoint = geoPoint;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Rota getRota() {
        return rota;
    }

    public void setRota(Rota rota) {
        this.rota = rota;
    }

    @Override
    public String toString() {
        return "UserLocation{" +
                "geoPoint=" + geoPoint +
                ", timestamp=" + timestamp +
                ", user=" + user +
                '}';
    }
}
