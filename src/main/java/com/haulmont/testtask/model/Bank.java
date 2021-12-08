package com.haulmont.testtask.model;

public class Bank {
    private long id;
    private String bankName;
    private Client client;
    private Credit credit;

    public Bank(long id, String bankName, Client client, Credit credit) {
        this.id = id;
        this.bankName = bankName;
        this.client = client;
        this.credit = credit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

    public String getClientName() {
        return this.getClient().getFullName();
    }

    public String getCreditLimitRate() {
        return this.credit.getCreditLimit() + " " + this.credit.getPercentRate() + "%";
    }
}
