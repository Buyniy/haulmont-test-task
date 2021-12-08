package com.haulmont.testtask.controller;

import com.haulmont.testtask.dao.BankDao;
import com.haulmont.testtask.dao.ClientDao;
import com.haulmont.testtask.dao.CreditDao;
import com.haulmont.testtask.dao.CreditOfferDao;
import com.haulmont.testtask.model.Bank;
import com.haulmont.testtask.model.Client;
import com.haulmont.testtask.model.Credit;
import com.haulmont.testtask.model.CreditOffer;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Supplier;

public class Controller {

    private static DBManager dbManager;
    private static BankDao bankDao;
    private static ClientDao clientDao;
    private static CreditDao creditDao;
    private  static CreditOfferDao creditOfferDao;

    private static void establishingConnection() {
        dbManager = new DBManager();
        bankDao = new BankDao(dbManager);
        clientDao = new ClientDao(dbManager);
        creditDao = new CreditDao(dbManager);
        creditOfferDao = new CreditOfferDao(dbManager);
    }

    private static void closingConnection() {
        try {
            dbManager.getConnection().close();
        } catch (SQLException e) {
            System.out.println("Ошибка при закрытии соединения с базой данных");
            e.printStackTrace();
        }
    }

    private static void establishingAndClosingConnection(Runnable runnable) {
        establishingConnection();
        runnable.run();
        closingConnection();
    }

    private static <T> T establishingAndClosingConnection(Supplier<T> supplier) {
        establishingConnection();
        T connection = supplier.get();
        closingConnection();
        return connection;
    }

    public static void addBank(Bank bank) {
        establishingAndClosingConnection(()-> bankDao.insert(bank));
    }

    public static void updateBank(Bank bank) {
        establishingAndClosingConnection(()-> bankDao.update(bank));
    }

    public static int deleteBank(long id) {
        return establishingAndClosingConnection(()-> bankDao.delete(id));
    }

    public static List<Bank> getBankList() {
       return establishingAndClosingConnection(()-> bankDao.getAll());
    }

    public static Bank getBankById(long id) {
        return establishingAndClosingConnection(()-> bankDao.getById(id));
    }

    public static void addClient(Client client) {
        establishingAndClosingConnection(()-> clientDao.insert(client));
    }

    public static void updateClient(Client client) {
        establishingAndClosingConnection(()-> clientDao.update(client));
    }

    public static int deleteClient(long id) {
        return establishingAndClosingConnection(()-> clientDao.delete(id));
    }

    public static List<Client> getClientList() {
        return establishingAndClosingConnection(()-> clientDao.getAll());
    }

    public static Client getClientById(long id) {
        return establishingAndClosingConnection(()-> clientDao.getById(id));
    }

    public static void addCredit(Credit credit) {
        establishingAndClosingConnection(()-> creditDao.insert(credit));
    }

    public static void updateCredit(Credit credit) {
        establishingAndClosingConnection(()-> creditDao.update(credit));
    }

    public static int deleteCredit(long id) {
        return establishingAndClosingConnection(()-> creditDao.delete(id));
    }

    public static List<Credit> getCreditList() {
        return establishingAndClosingConnection(()-> creditDao.getAll());
    }

    public static Credit getCreditById(long id) {
        return establishingAndClosingConnection(()-> creditDao.getById(id));
    }

    public static void addCreditOffer(CreditOffer creditOffer) {
        establishingAndClosingConnection(()-> creditOfferDao.insert(creditOffer));
    }

    public static void updateCreditOffer(CreditOffer creditOffer) {
        establishingAndClosingConnection(()-> creditOfferDao.update(creditOffer));
    }

    public static int deleteCreditOffer(long id) {
        return establishingAndClosingConnection(()-> creditOfferDao.delete(id));
    }

    public static List<CreditOffer> getCreditOfferList() {
        return establishingAndClosingConnection(()-> creditOfferDao.getAll());
    }

    public static CreditOffer getCreditOfferById(long id) {
        return establishingAndClosingConnection(()-> creditOfferDao.getById(id));
    }
}