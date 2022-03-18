package com.tingyu.model;

import java.io.Serializable;

public class Person implements Serializable {

    private String personId;

    private String name;

    private String number;

    public Person() {
    }

    public Person(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId='" + personId + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
