package com.example.matteoaldini.lessonmanager.model;

import java.io.Serializable;

/**
 * Created by matteo.aldini on 04/05/2015.
 */
public class Student implements Serializable {
    private String name;
    private String surname;
    private String phone;
    private String email;
    private int color;
    private long id;
    //private Byte[] image;

    public Student(String name, String surname, String phone, String email, int color) {
        this.email = email;
        this.phone = phone;
        this.surname = surname;
        this.name = name;
        this.color = color;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
