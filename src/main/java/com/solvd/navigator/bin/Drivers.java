package com.solvd.navigator.bin;

public class Drivers {
    private int driver_Id;
    private int employee_Id;
    private int vechicle_Id;

    private Drivers(Builder builder) {
        this.driver_Id = builder.driver_Id;
        this.employee_Id = builder.employee_Id;
        this.vechicle_Id = builder.vechicle_Id;
    }
    public int getDriverId() {
        return driver_Id;
    }

    public int getEmployeeId() {
        return employee_Id;
    }

    public int getVehicleId() {
        return vechicle_Id;
    }

    // Builder class
    public static class Builder {
        private int driver_Id;
        private int employee_Id;
        private int vechicle_Id;

        // Setter methods for each parameter
        public Builder setDriverId(int driver_Id) {
            this.driver_Id = driver_Id;
            return this;
        }

        public Builder setEmployeeId(int employee_Id) {
            this.employee_Id = employee_Id;
            return this;
        }

        public Builder setVehicleId(int vechicle_Id) {
            this.vechicle_Id = vechicle_Id;
            return this;
        }

        // Build method to create an instance of Drivers
        public Drivers build() {
            return new Drivers(this);
        }
    }


}
