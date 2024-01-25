package com.solvd.navigator.bin;

public class Employee {
    private int employeeId;
    private String role;
    private int personId;

    public Employee() {

    }


    public Employee(int employeeId, String role, int personId) {
        this.employeeId = employeeId;
        this.role = role;
        this.personId = personId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    @Override
    public String toString() {
        return "Employees{" +
               "employeeId=" + employeeId +
               ", role='" + role + '\'' +
               ", personId=" + personId +
               '}';
    }
}

