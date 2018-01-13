package com.ahmet.showmustgoon.model;


import java.util.ArrayList;

public class MapObjectContainer
{
    private ArrayList<MapObjectModel> container;

    public MapObjectContainer()
    {
        container = new ArrayList<MapObjectModel>();
    }


    public void addObject(MapObjectModel object)
    {
        container.add(object);
    }


    public void removeObject(MapObjectModel object)
    {
        container.remove(object);
    }


    public MapObjectModel getObject(int index)
    {
        return container.get(index);
    }


    public MapObjectModel getObjectById(int id)
    {
        for (MapObjectModel model:container) {
            if (model.getId() == id) {
                return model;
            }
        }
        return null;
    }

    public int size()
    {
        return container.size();
    }

}
