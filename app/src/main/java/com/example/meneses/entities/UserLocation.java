package com.example.meneses.entities;


import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class UserLocation {

    private LatLng geoPoint;

    private Date timestamp;
    private User user;

    public UserLocation() {
    }

    public UserLocation(LatLng geoPoint, Date timestamp, User user) {
        this.geoPoint = geoPoint;
        this.timestamp = timestamp;
        this.user = user;
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

    @Override
    public String toString() {
        return "UserLocation{" +
                "geoPoint=" + geoPoint +
                ", timestamp=" + timestamp +
                ", user=" + user +
                '}';
    }
}
