package com.haulmont.testtask.dao;

import com.haulmont.testtask.controller.DBManager;
import com.haulmont.testtask.model.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class ClientDao implements Dao<Client> {

    private final DBManager dbManager;
    private final String TABLE_NAME = "Client";

    public ClientDao(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public void insert(Client client) {
        PreparedStatement statement;
        try {
            String sql = "INSERT INTO " + TABLE_NAME
                    + " (fullName, phoneNumber, emailAddress, passportNumber) VALUES (?, ?, ?, ?)";
            statement = dbManager.getConnection().prepareStatement(sql);
            statement.setString(1, client.getFullName());
            statement.setString(2, client.getPhoneNumber());
            statement.setString(3, client.getEmailAddress());
            statement.setString(4, client.getPassportNumber());
            statement.execute();
            System.out.println("Клиент " + client.getId() + " успешно добавлен");
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при добавлении клиента");
            e.printStackTrace();
        }
    }

    @Override
    public void update(Client client) {
        PreparedStatement statement;
        try {
            String sql = "UPDATE " + TABLE_NAME
                    + " SET fullName=?, phoneNumber=?, emailAddress=?, passportNumber=? WHERE Client_Id=?";
            statement = dbManager.getConnection().prepareStatement(sql);
            statement.setString(1, client.getFullName());
            statement.setString(2, client.getPhoneNumber());
            statement.setString(3, client.getEmailAddress());
            statement.setString(4, client.getPassportNumber());
            statement.setLong(5, client.getId());
            statement.execute();
            System.out.println("Клиент " + client.getId() + " успешно изменен");
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при изменении клиента");
            e.printStackTrace();
        }
    }

    @Override
    public int delete(long id) {
        PreparedStatement statement;
        try {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE Client_Id =?";
            statement = dbManager.getConnection().prepareStatement(sql);
            statement.setLong(1, id);
            statement.execute();
            System.out.println("Клиент " + id + " успешно удален");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Нельзя удалить клиента, для которого существует кредитное предложение или банк");
            return -1;
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при удалении клиента");
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    @Override
    public List<Client> getAll() {
        PreparedStatement statement;
        List<Client> resultList = new ArrayList<>();
        try {
            statement = dbManager.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                resultList.add(new Client(
                        resultSet.getLong("Client_Id"),
                        resultSet.getString("fullName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("emailAddress"),
                        resultSet.getString("passportNumber")
                ));
            }
            System.out.println("Все клиенты успешно выбраны");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Возникла ошибка при получении списка клиентов");
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public Client getById(long id) {
        PreparedStatement statement;
        Client result;
        try {
            statement = dbManager.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME
                    + " WHERE Client_Id=?");
            statement.setLong(1, id);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                result = new Client(
                        resultSet.getLong("Client_Id"),
                        resultSet.getString("fullName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("emailAddress"),
                        resultSet.getString("passportNumber")
                );
                System.out.println("Клиент" + id + " успешно выбран");
                return result;
            }
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при получении клиента");
            e.printStackTrace();
        }
        return null;
    }
}
