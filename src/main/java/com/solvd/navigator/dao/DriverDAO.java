package com.solvd.navigator.dao;

import com.solvd.navigator.bin.Driver;


public interface DriverDAO extends AbstractDAO<Driver> {
    @Override
    int create(Driver driver);

    @Override
    Driver getById(int id);

    @Override
    void update(Driver driver);

    void delete(int id);
}
