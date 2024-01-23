package com.solvd.navigator.dao;

import com.solvd.navigator.bin.Employee;

public interface EmployeeDAO extends AbstractDAO<Employee>{
    @Override
    int create(Employee employee);

    @Override
    Employee getById(int id);

    @Override
    void update(Employee employee);

    void delete(int id);
}
