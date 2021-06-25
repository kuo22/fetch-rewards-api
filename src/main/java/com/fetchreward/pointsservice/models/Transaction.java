package com.fetchreward.pointsservice.models;

import java.util.Date;

public class Transaction {
    private final int id;
    private final String payer;
    private int points;
    private final Date timestamp;

    public Transaction(int id, String payer, int points, Date timestamp) {
        this.id = id;
        this.payer = payer;
        this.points = points;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getPayer() {
        return payer;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
