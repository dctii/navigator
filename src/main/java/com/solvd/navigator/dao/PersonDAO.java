package com.solvd.navigator.dao;

import com.solvd.navigator.bin.Person;

public interface PersonDAO extends AbstractDAO<Person>{
    @Override
    int create(Person person);

    @Override
    Person getById(int id);

    @Override
    void update(Person person);

    void delete(int id);
}
