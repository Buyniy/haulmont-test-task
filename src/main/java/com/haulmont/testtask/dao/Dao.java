package com.haulmont.testtask.dao;

import java.util.List;

public interface Dao<T> {
    void insert(T entity);

    void update(T entity);

    int delete(long id);

    List<T> getAll();

    T getById(long id);
}
