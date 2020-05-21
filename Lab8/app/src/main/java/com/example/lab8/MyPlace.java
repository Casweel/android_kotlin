package com.example.lab8;

import com.google.android.libraries.places.api.model.Place;

import java.io.Serializable;

public class MyPlace implements Serializable
{
    public Place place;
    public  MyPlace(Place place)
    {
        this.place = place;
    }
}
