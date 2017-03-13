package com.heinz.householdhelper.bt;

import java.io.Serializable;
import java.util.Date;

/**
 * Content holder.
 * Created by Heinz on 10.03.2017.
 */

public class FreetecMeasure implements Serializable {

    private Date time;
    private String name;
    private String address;
    private String temperature;
    private int battery;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    @Override
    public String toString() {
        return "Device: " + getName() + ", measured: " + getTemperature() + ", at " + getTime() + " with battery: " + getBattery() + ".";
    }
}
