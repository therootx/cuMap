package com.ahmet.showmustgoon.model;


import android.location.Location;

public class MapObjectModel
{
    private int x;
    private int y;
    private int id;
    private String caption;
    private Location location;

    public MapObjectModel(int id, Location location, String caption)
    {
        this.location = location;
        this.caption = caption;
        this.id = id;
    }

    public MapObjectModel(int id, int x, int y, String caption)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.caption = caption;
    }

    public int getId()
    {
        return id;
    }


    public int getX()
    {
        return x;
    }


    public int getY()
    {
        return y;
    }


    public Location getLocation()
    {
        return location;
    }


    public String getCaption()
    {
        return caption;
    }

}
