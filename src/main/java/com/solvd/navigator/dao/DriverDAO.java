package com.solvd.navigator.dao;

import com.solvd.navigator.bin.Driver;

import java.util.List;


public interface DriverDAO extends AbstractDAO<Driver> {
    @Override
    int create(Driver driver);

    @Override
    Driver getById(int id);
    List<Driver> getAll();

    @Override
    void update(Driver driver);

    void delete(int id);
}
