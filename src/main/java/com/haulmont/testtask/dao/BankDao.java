package com.haulmont.testtask.dao;

import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.controller.DBManager;
import com.haulmont.testtask.model.Bank;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankDao implements Dao<Bank> {

    private final DBManager dbManager;
    private final String TABLE_NAME = "Bank";

    public BankDao(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public void insert(Bank bank) {
        PreparedStatement statement;
        try {
            String sql = "INSERT INTO " + TABLE_NAME + " (bankName, client, credit) VALUES (?, ?, ?)";
            statement = dbManager.getConnection().prepareStatement(sql);
            statement.setString(1, bank.getBankName());
            statement.setLong(2, bank.getClient().getId());
            statement.setLong(3, bank.getCredit().getId());
            statement.execute();
            System.out.println("Банк " + bank.getId() + " успешно добавлен");
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при добавлении банка");
            e.printStackTrace();
        }
    }

    @Override
    public void update(Bank bank) {
        PreparedStatement statement;
        try {
            String sql = "UPDATE " + TABLE_NAME + " SET bankName=?, client=?, credit=? WHERE Bank_Id=?";
            statement = dbManager.getConnection().prepareStatement(sql);
            statement.setString(1, bank.getBankName());
            statement.setLong(2, bank.getClient().getId());
            statement.setLong(3, bank.getCredit().getId());
            statement.setLong(4, bank.getId());
            statement.execute();
            System.out.println("Банк " + bank.getId() + " успешно изменен");
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при изменении банка");
            e.printStackTrace();
        }
    }

    @Override
    public int delete(long id) {
        PreparedStatement statement;
        try {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE Bank_Id =?";
            statement = dbManager.getConnection().prepareStatement(sql);
            statement.setLong(1, id);
            statement.execute();
            System.out.println("Банк " + id + " успешно удален");
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при удалении банка");
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    @Override
    public List<Bank> getAll() {
        PreparedStatement statement;
        List<Bank> resultList = new ArrayList<>();
        try {
            statement = dbManager.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                resultList.add(new Bank(
                        resultSet.getLong("Bank_Id"),
                        resultSet.getString("bankName"),
                        Controller.getClientById(resultSet.getLong("Client")),
                        Controller.getCreditById(resultSet.getLong("Credit"))
                ));
            }
            System.out.println("Все банки успешно выбраны");
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при получении списка банков");
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public Bank getById(long id) {
        PreparedStatement statement;
        Bank result;
        try {
            statement = dbManager.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME
                    + " WHERE Bank_Id=?");
            statement.setLong(1, id);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                result = new Bank(
                        resultSet.getLong("Bank_Id"),
                        resultSet.getString("bankName"),
                        Controller.getClientById(resultSet.getLong("Client")),
                        Controller.getCreditById(resultSet.getLong("Credit"))
                );
                System.out.println("Банк" + id + " успешно выбран");
                return result;
            }
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при получении банка");
            e.printStackTrace();
        }
        return null;
    }
}
