package com.solvd.navigator.dao;

import com.solvd.navigator.bin.Storage;

import java.util.List;

public interface StorageDAO extends AbstractDAO<Storage> {
    @Override
    int create(Storage storage);

    @Override
    Storage getById(int storageId);

    List<Storage> getAll();
    Storage getStorageByLocationId(int locationId);

    @Override
    void update(Storage storage);

    @Override
    void delete(int storageId);
}
