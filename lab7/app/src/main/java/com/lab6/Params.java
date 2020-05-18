package com.lab6;

import android.content.Context;
import android.widget.ListView;
import android.widget.Spinner;

public class Params {
    private Context context;
    private Products product;
    private Context base;
    private String name;
    private Integer count;
    private Double price;
    private ListView scrollView;
    private Spinner nameUpd;

    Params(Context context, Context base, Products product, ListView scrollView) {
        this.context = context;
        this.base = base;
        this.product = product;
        this.scrollView = scrollView;
    }

    Params(Context context, Context base, String name, Integer count, Double price, Spinner nameUpd) {
        this.context = context;
        this.base = base;
        this.name = name;
        this.count = count;
        this.price = price;
        this.nameUpd = nameUpd;
    }

    public Context getContext() {
        return context;
    }

    public Products getProduct() {
        return product;
    }

    public Context getBase() {
        return base;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getCount() {
        return count;
    }

    public ListView getScrollView() {
        return scrollView;
    }

    public Spinner getNameUpd() {
        return nameUpd;
    }
}
