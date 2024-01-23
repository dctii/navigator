package com.solvd.navigator.dao;

import com.solvd.navigator.bin.Storage;

public interface StorageDAO extends AbstractDAO<Storage> {
    @Override
    int create(Storage storage);

    @Override
    Storage getById(int storageId);

    @Override
    void update(Storage storage);

    @Override
    void delete(int storageId);
}
