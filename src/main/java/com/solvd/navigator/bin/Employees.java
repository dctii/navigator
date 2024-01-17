package com.solvd.navigator.bin;

public class Employees {
    public class Employess {
        private int employeeID;
        private String role;
        private int personId;


        public Employess(int employeeID, String role, int personId) {
            this.employeeID = employeeID;
            this.role = role;
            this.personId = personId;
        }

        public int getEmployeeID() {
            return employeeID;
        }

        public void setEmployeeID(int employeeID) {
            this.employeeID = employeeID;
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
            return "Employess{" +
                   "employeeID=" + employeeID +
                   ", role='" + role + '\'' +
                   ", personId=" + personId +
                   '}';
        }
    }
}
