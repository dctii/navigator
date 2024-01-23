package com.solvd.navigator.dao;

import com.solvd.navigator.bin.Vehicle;

public interface VehicleDAO extends AbstractDAO<Vehicle> {

    @Override
    int create(Vehicle vehicle);

    @Override
    Vehicle getById(int id);

    @Override
    void update(Vehicle vehicle);

    @Override
    void delete(int id);
}