package com.example.dietapp;

public class FoodViewItem {
    private String id, name, cal, car, pro, fat, review, time, place, img, quantity;

    public FoodViewItem(String ID, String name, String cal, String car, String pro, String fat, String quantity, String review, String time, String place, String img){
        setId(ID);
        setName(name);
        setCal(cal);
        setCar(car);
        setPro(pro);
        setFat(fat);
        setPlace(place);
        setReview(review);
        setTime(time);
        setImg(img);
        setQuantity(quantity);
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public String getCal() {
        return cal;
    }

    public String getCar() {
        return car;
    }

    public String getFat() {
        return fat;
    }

    public String getPro() {
        return pro;
    }

    public String getPlace() {
        return place;
    }

    public String getReview() {
        return review;
    }

    public String getTime() {
        return time;
    }

    public String getImg() {
        return img;
    }
}
