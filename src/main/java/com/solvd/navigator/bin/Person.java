package com.solvd.navigator.bin;

import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class Person {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.PERSON);
    private int personId;
    private String firstName;
    private String lastName;
    public Person() {

    }

    public Person(int personId, String firstName, String lastName) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return personId == person.personId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId);
    }

    @Override
    public String toString() {
        Class<?> currClass = ClassConstants.PERSON;
        String[] fieldNames = {
                "personId",
                "firstName",
                "lastName"
        };

        String fieldsString =
                StringFormatters.buildFieldsString(this, fieldNames);

        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }

}
