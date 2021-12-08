package com.haulmont.testtask.model;

public class Credit {
    private long id;
    private double creditLimit;
    private float percentRate;

    public Credit(long id, double creditLimit, float percentRate) {
        this.id = id;
        this.creditLimit = creditLimit;
        this.percentRate = percentRate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public float getPercentRate() {
        return percentRate;
    }

    public void setPercentRate(float percentRate) {
        this.percentRate = percentRate;
    }
}
