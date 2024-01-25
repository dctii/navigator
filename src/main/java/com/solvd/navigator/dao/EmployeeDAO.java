package com.solvd.navigator.dao;

import com.solvd.navigator.bin.Employee;
import java.util.List;

public interface EmployeeDAO extends AbstractDAO<Employee>{
    @Override
    int create(Employee employee);

    @Override
    Employee getById(int id);

    List<Employee> getAll();

    @Override
    void update(Employee employee);

    void delete(int id);
}
