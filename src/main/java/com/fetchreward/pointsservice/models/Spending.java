package com.fetchreward.pointsservice.models;

public class Spending {
    private String payer;
    private int points;

    public Spending(String payer, int points) {
        this.payer = payer;
        this.points = points;
    }

    public String getPayer() {
        return payer;
    }

    public int getPoints() {
        return points;
    }
}
