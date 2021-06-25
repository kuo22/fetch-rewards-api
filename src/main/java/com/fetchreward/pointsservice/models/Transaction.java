package com.fetchreward.pointsservice.models;

import java.util.Date;

public class Transaction {
    private final String payer;
    private int points;
    private final Date timestamp;

    public Transaction(String payer, int points, Date timestamp) {
        this.payer = payer;
        this.points = points;
        this.timestamp = timestamp;
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
