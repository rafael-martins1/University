package com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.model;

public class EventParticipants {
    private int eventId;
    private String participantUsername;
    private String participantRealName;
    private String participantType;
    private String participantGender;
    private String participantSituation;
    private boolean isPaid;

    private double time;

    private java.sql.Time timestamp;

    public EventParticipants() {
    }

    public EventParticipants(int eventId, String participantUsername, String participantRealName, String participantType, String participantGender, String participantSituation) {
        this.eventId = eventId;
        this.participantUsername = participantUsername;
        this.participantRealName = participantRealName;
        this.participantType = participantType;
        this.participantGender = participantGender;
        this.participantSituation = participantSituation;
        this.isPaid = false;
        this.time = 0;
    }

    public EventParticipants(int eventId, String participantUsername, String participantRealName, String participantType, String participantGender) {
        this.eventId = eventId;
        this.participantUsername = participantUsername;
        this.participantRealName = participantRealName;
        this.participantType = participantType;
        this.participantGender = participantGender;
        this.participantSituation = null;
        this.timestamp = null;
        this.isPaid = false;
        this.time = 0;
    }


    public int getEventId() {
        return eventId;
    }

    public String getParticipantUsername() {
        return participantUsername;
    }

    public void setParticipantUsername(String participantUsername) {
        this.participantUsername = participantUsername;
    }

    public String getParticipantRealName() {
        return participantRealName;
    }

    public void setParticipantRealName(String participantRealName) {
        this.participantRealName = participantRealName;
    }

    public String getParticipantType() {
        return participantType;
    }

    public void setParticipantType(String participantType) {
        this.participantType = participantType;
    }

    public String getParticipantGender() {
        return participantGender;
    }

    public void setParticipantGender(String participantGender) {
        this.participantGender = participantGender;
    }

    public String getParticipantSituation() {
        return participantSituation;
    }

    public void setParticipantSituation(String participantSituation) {
        this.participantSituation = participantSituation;
    }

    public java.sql.Time getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(java.sql.Time timestamp) {
        this.timestamp = timestamp;
    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public void setTime(long time){this.time = time;}

    public double getTime (){return time;}
}
