package com.Models;

public class Product {
    private int id;
    private String name;
    private String description;
    private String imgName;

    public Product() { }

    public Product(int id, String name, String description, String imgName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imgName = imgName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
