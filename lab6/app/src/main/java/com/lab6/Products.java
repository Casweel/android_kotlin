package com.lab6;

public class Products {
    private int id;
    private String name;
    private int count;
    private double price;

    Products(int id,  String name, int count, double price)
    {
        this.id = id;
        this.name = name;
        this.count = count;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getCount() {
        return count;
    }

    public double getPrice() {
        return price;
    }
}
