package com.example.dietapp;

import java.io.Serializable;

public class InputItem implements Serializable {

    int when;
    String name;
    int cal;
    int car;
    int pro;
    int fat;
    public InputItem(int when, String name, int cal, int car, int pro, int fat) {

        this.when = when;
        this.name = name;
        this.cal = cal;
        this.car = car;
        this.pro = pro;
        this.fat = fat;
    }

    public int getWhen() {
        return when;
    }
    public String getName() {
        return name;
    }
    public int getCal() {
        return cal;
    }
    public int getCar() {
        return car;
    }
    public int getPro() {
        return pro;
    }
    public int getFat() {
        return fat;
    }


    public void setWhen(int when) {
        this.when = when;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCal(int cal) {
        this.cal = cal;
    }
    public void setCar(int car) {
        this.car = car;
    }
    public void setPro(int pro) {
        this.pro = pro;
    }
    public void setFat(int fat) {
        this.fat = fat;
    }

}
