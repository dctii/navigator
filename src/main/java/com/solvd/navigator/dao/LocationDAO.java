package com.solvd.navigator.dao;

import com.solvd.navigator.bin.Location;

public interface LocationDAO extends AbstractDAO<Location> {
    @Override
    int create(Location location);

    @Override
    Location getById(int id);

    @Override
    void update(Location location);

    void delete(int id);
}
