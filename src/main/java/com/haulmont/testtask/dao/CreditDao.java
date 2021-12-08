package com.haulmont.testtask.dao;

import com.haulmont.testtask.controller.DBManager;
import com.haulmont.testtask.model.Credit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class CreditDao implements Dao<Credit> {

    private final DBManager dbManager;
    private final String TABLE_NAME = "Credit";

    public CreditDao(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public void insert(Credit credit) {
        PreparedStatement statement;
        try {
            String sql = "INSERT INTO " + TABLE_NAME + " (creditLimit, percentRate) VALUES (?, ?)";
            statement = dbManager.getConnection().prepareStatement(sql);
            statement.setDouble(1, credit.getCreditLimit());
            statement.setDouble(2, credit.getPercentRate());
            statement.execute();
            System.out.println("Кредит " + credit.getId() + " успешно добавлен");
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при добавлении кредита");
            e.printStackTrace();
        }
    }

    @Override
    public void update(Credit credit) {
        PreparedStatement statement;
        try {
            String sql = "UPDATE " + TABLE_NAME + " SET creditLimit=?, percentRate=? WHERE Credit_Id=?";
            statement = dbManager.getConnection().prepareStatement(sql);
            statement.setDouble(1, credit.getCreditLimit());
            statement.setDouble(2, credit.getPercentRate());
            statement.setLong(3, credit.getId());
            statement.execute();
            System.out.println("Кредит " + credit.getId() + " успешно изменен");
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при изменении кредита");
            e.printStackTrace();
        }
    }

    @Override
    public int delete(long id) {
        PreparedStatement statement;
        try {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE Credit_Id =?";
            statement = dbManager.getConnection().prepareStatement(sql);
            statement.setLong(1, id);
            statement.execute();
            System.out.println("Кредит " + id + " успешно удален");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Нельзя удалить клиента, для которого существует кредитное предложение или банк");
            e.printStackTrace();
            return -1;
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при удалении кредита");
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    @Override
    public List<Credit> getAll() {
        PreparedStatement statement;
        List<Credit> resultList = new ArrayList<>();
        try {
            statement = dbManager.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                resultList.add(new Credit(
                        resultSet.getLong("Credit_Id"),
                        resultSet.getDouble("creditLimit"),
                        resultSet.getFloat("percentRate")
                ));
            }
            System.out.println("Все кредиты успешно выбраны");
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при получении списка кредитов");
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public Credit getById(long id) {
        PreparedStatement statement;
        Credit result;
        try {
            statement = dbManager.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME
                    + " WHERE Credit_Id=?");
            statement.setLong(1, id);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                result = new Credit(
                        resultSet.getLong("Credit_Id"),
                        resultSet.getDouble("creditLimit"),
                        resultSet.getFloat("percentRate")
                );
                System.out.println("Кредит" + id + " успешно выбран");
                return result;
            }
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при получении кредита");
            e.printStackTrace();
        }
        return null;
    }
}