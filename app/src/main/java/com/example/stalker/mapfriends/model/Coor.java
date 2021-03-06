package com.example.stalker.mapfriends.model;

import java.sql.Timestamp;
import java.util.Date;

public class Coor {
    private long id;
    private double  longitude;
    private double  latitude;
    private Date date;

    public Coor() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public <T> T getDataBaseDate() {
        return (T) new Long( date.getTime() );
    }

    public <T> void setDataBaseDate(T date) {
        setDate(new Date());
    }

    @Override
    public String toString() {
        return "Coor{" +
                "id=" + id +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", date='" + date + '\'' +
                '}';
    }

}
