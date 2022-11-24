package com.example.dietapp;

public class SingleItem {
    private Integer standard;
    private Integer weight;
    private Integer calVal;
    private Float carVal;
    private Float proVal;
    private Float fatVal;
    private String name;
    private String brand;

    public SingleItem(String name, String brand, Integer standard, Integer weight, Integer cal, Float car, Float pro, Float fat){
        setStandard(standard);
        setWeight(weight);
        setName(name);
        setBrand(brand);
        setCalVal(cal);
        setCarVal(car);
        setProVal(pro);
        setFatVal(fat);
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStandard(Integer standard) {
        this.standard = standard;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setCalVal(Integer calVal) {
        this.calVal = calVal;
    }

    public void setCarVal(Float carVal) {
        this.carVal = carVal;
    }

    public void setProVal(Float proVal) {
        this.proVal = proVal;
    }

    public void setFatVal(Float fatVal) {
        this.fatVal = fatVal;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public Integer getCalVal() {
        return calVal;
    }

    public Float getCarVal() {
        return carVal;
    }

    public Float getProVal() {
        return proVal;
    }

    public Float getFatVal() {
        return fatVal;
    }

    public Integer getStandard() {
        return standard;
    }

    public Integer getWeight() {
        return weight;
    }
}
