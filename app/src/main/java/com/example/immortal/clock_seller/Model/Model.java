package com.example.immortal.clock_seller.Model;

import java.io.Serializable;

public class Model implements Serializable {

    private String name, b_name, detail, image;
    private Integer price, quantity;

    public Model() {

    }

    public Model(String name, String b_name, String detail, String image, Integer price, Integer quantity) {
        this.name = name;
        this.b_name = b_name;
        this.detail = detail;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getB_name() {
        return b_name;
    }

    public void setB_name(String b_name) {
        this.b_name = b_name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}