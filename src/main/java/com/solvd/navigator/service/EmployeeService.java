package com.solvd.navigator.service;

import com.solvd.navigator.bin.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    Employee getEmployeeById(int employeeId);


    List<Employee> getEmployeesByRole(String role);

}
