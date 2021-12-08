package com.haulmont.testtask.model;

import java.time.LocalDate;

public class CreditOffer {
    private long id;
    private Client client;
    private Credit credit;
    private double creditAmount;
    private int countPayment;
    private LocalDate dateCreditOffer;
    private LocalDate datePayment;
    private double paymentAmount;
    private double bodyAmount;
    private double percentAmount;

    public CreditOffer(long id, Client client, Credit credit, double creditAmount, int countPayment,
                       LocalDate dateCreditOffer, LocalDate datePayment, double paymentAmount,
                       double bodyAmount, double percentAmount) {
        this.id = id;
        this.client = client;
        this.credit = credit;
        this.creditAmount = creditAmount;
        this.countPayment = countPayment;
        this.dateCreditOffer = dateCreditOffer;
        this.datePayment = datePayment;
        this.paymentAmount = paymentAmount;
        this.bodyAmount = bodyAmount;
        this.percentAmount = percentAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public double getCreditAmount() {
        return Math.round(creditAmount);
    }

    public void setCreditAmount(double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public int getCountPayment() {
        return countPayment;
    }

    public void setCountPayment(int countPayment) {
        this.countPayment = countPayment;
    }

    public LocalDate getDateCreditOffer() {
        return dateCreditOffer;
    }

    public void setDateCreditOffer(LocalDate dateCreditOffer) {
        this.dateCreditOffer = dateCreditOffer;
    }

    public LocalDate getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(LocalDate datePayment) {
        this.datePayment = datePayment;
    }

    public double getPaymentAmount() {
        return Math.round(paymentAmount);
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public double getBodyAmount() {
        return Math.round(bodyAmount);
    }

    public void setBodyAmount(double bodyAmount) {
        this.bodyAmount = bodyAmount;
    }

    public double getPercentAmount() {
        return Math.round(percentAmount);
    }

    public void setPercentAmount(double percentAmount) {
        this.percentAmount = percentAmount;
    }

    public String getClientName() {
        return this.client.getFullName();
    }

    public Double getCreditLimit() {
        return this.credit.getCreditLimit();
    }

    public String getCreditLimitRate() {
        return this.credit.getCreditLimit() + " " + this.credit.getPercentRate() + "%";
    }
}