package com.haulmont.testtask.controller;

import org.hsqldb.jdbc.JDBCDataSource;

import java.io.*;
import java.sql.*;

public class DBManager {
    private final String DB_NAME = "testDB";
    private final String DB_PATH = "src/main/resources/" + DB_NAME;
    private final String DB_URL = "jdbc:hsqldb:file:" + DB_PATH;
    private final JDBCDataSource dataSource;
    private Connection connection = null;

    public Connection getConnection() {
        return this.connection;
    }

    public DBManager() {
        //Проверка наличия драйвера БД
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер JDBC не обнаружен");
            e.printStackTrace();
        }
        //Установка значений
        dataSource = new JDBCDataSource();
        dataSource.setDatabaseName(DB_NAME);
        dataSource.setURL(DB_URL);
        dataSource.setUser("User");
        dataSource.setPassword("");
        //Соединение
        try {
            connection = dataSource.getConnection();
        }
        catch (SQLException e){
            System.out.println("Не удалось установить соединение с базой данных");
            e.printStackTrace();
        }
    }
}