package com.haulmont.testtask.dao;

import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.controller.DBManager;
import com.haulmont.testtask.model.CreditOffer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreditOfferDao implements Dao<CreditOffer> {

    private final DBManager dbManager;
    private final String TABLE_NAME = "CreditOffer";

    public CreditOfferDao(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public void insert(CreditOffer creditOffer) {
        PreparedStatement statement;
        try {
            String sql = "INSERT INTO " + TABLE_NAME
                    + " (client, credit, creditAmount, countPayment, dateCreditOffer, datePayment, paymentAmount, " +
                    "bodyAmount, percentAmount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            statement = dbManager.getConnection().prepareStatement(sql);
            statement.setLong(1, creditOffer.getClient().getId());
            statement.setLong(2, creditOffer.getCredit().getId());
            statement.setDouble(3, creditOffer.getCreditAmount());
            statement.setInt(4, creditOffer.getCountPayment());
            statement.setString(5, creditOffer.getDateCreditOffer().toString());
            statement.setString(6, creditOffer.getDatePayment().toString());
            statement.setDouble(7, creditOffer.getPaymentAmount());
            statement.setDouble(8, creditOffer.getBodyAmount());
            statement.setDouble(9, creditOffer.getPercentAmount());
            statement.execute();
            System.out.println("Кредитное предложение " + creditOffer.getId() + " успешно добавлено");
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при добавлении кредитного предложения");
            e.printStackTrace();
        }
    }

    @Override
    public void update(CreditOffer creditOffer) {
        PreparedStatement statement;
        try {
            String sql = "UPDATE " + TABLE_NAME
                    + " SET client=?, credit=?, creditAmount=?, countPayment=?, dateCreditOffer=?, datePayment=?," +
                    "paymentAmount=?, bodyAmount=?, percentAmount=? WHERE CreditOffer_Id=?";
            statement = dbManager.getConnection().prepareStatement(sql);
            statement.setLong(1, creditOffer.getClient().getId());
            statement.setLong(2, creditOffer.getCredit().getId());
            statement.setDouble(3, creditOffer.getCreditAmount());
            statement.setInt(4, creditOffer.getCountPayment());
            statement.setString(5, creditOffer.getDateCreditOffer().toString());
            statement.setString(6, creditOffer.getDatePayment().toString());
            statement.setDouble(7, creditOffer.getPaymentAmount());
            statement.setDouble(8, creditOffer.getBodyAmount());
            statement.setDouble(9, creditOffer.getPercentAmount());
            statement.execute();
            System.out.println("Кредитное предложение " + creditOffer.getId() + " успешно изменено");
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при изменении кредитного предложения");
            e.printStackTrace();
        }
    }

    @Override
    public int delete(long id) {
        PreparedStatement statement;
        try {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE CreditOffer_Id =?";
            statement = dbManager.getConnection().prepareStatement(sql);
            statement.setLong(1, id);
            statement.execute();
            System.out.println("Кредитное предложение " + id + " успешно удалено");
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при удалении кредитного предложения");
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    @Override
    public List<CreditOffer> getAll() {
        PreparedStatement statement;
        List<CreditOffer> resultList = new ArrayList<>();
        try {
            statement = dbManager.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                resultList.add(new CreditOffer(
                        resultSet.getLong("CreditOffer_Id"),
                        Controller.getClientById(resultSet.getLong("Client")),
                        Controller.getCreditById(resultSet.getLong("Credit")),
                        resultSet.getDouble("creditAmount"),
                        resultSet.getInt("countPayment"),
                        resultSet.getDate("dateCreditOffer").toLocalDate(),
                        resultSet.getDate("datePayment").toLocalDate(),
                        resultSet.getDouble("paymentAmount"),
                        resultSet.getDouble("bodyAmount"),
                        resultSet.getDouble("percentAmount")
                ));
            }
            System.out.println("Все кредитные предложения успешно выбраны");
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при получении списка кредитных предложений");
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public CreditOffer getById(long id) {
        PreparedStatement statement;
        CreditOffer result;
        try {
            statement = dbManager.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME
                    + " WHERE CreditOffer_Id=?");
            statement.setLong(1, id);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                result = new CreditOffer(
                        resultSet.getLong("CreditOffer_Id"),
                        Controller.getClientById(resultSet.getLong("Client")),
                        Controller.getCreditById(resultSet.getLong("Credit")),
                        resultSet.getDouble("creditAmount"),
                        resultSet.getInt("countPayment"),
                        resultSet.getDate("dateCreditOffer").toLocalDate(),
                        resultSet.getDate("datePayment").toLocalDate(),
                        resultSet.getDouble("paymentAmount"),
                        resultSet.getDouble("bodyAmount"),
                        resultSet.getDouble("percentAmount")
                );
                System.out.println("Кредитное предложение" + id + " успешно выбрано");
                return result;
            }
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при получении кредитного предложения");
            e.printStackTrace();
        }
        return null;
    }
}