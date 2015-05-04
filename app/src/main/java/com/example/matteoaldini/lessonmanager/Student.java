package com.example.matteoaldini.lessonmanager;

/**
 * Created by matteo.aldini on 04/05/2015.
 */
public class Student {
    private String name;
    private String surname;
    private BirthDate birthDate;
    private String phone;
    private String email;

    public Student(String email, String phone, BirthDate birthDate, String surname, String name) {
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.surname = surname;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BirthDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(BirthDate birthDate) {
        this.birthDate = birthDate;
    }
}
