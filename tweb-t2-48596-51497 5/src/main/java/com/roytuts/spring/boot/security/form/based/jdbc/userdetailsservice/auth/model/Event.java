package com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.model;

import java.util.Date;

public class Event {
    private final String eventName;
    private final String eventDescription;
    private final Date eventDate;
    private final double joinValue;

    public Event(String eventName, String eventDescription, Date eventDate, double joinValue) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.joinValue = joinValue;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public double getJoinValue() {
        return joinValue;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", eventDate=" + eventDate +
                ", joinValue=" + joinValue +
                '}';
    }
}


