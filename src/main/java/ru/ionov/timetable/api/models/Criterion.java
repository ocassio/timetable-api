package ru.ionov.timetable.api.models;


import java.io.Serializable;

public class Criterion implements Serializable
{
    private String id;
    private String name;

    public Criterion()
    {
    }

    public Criterion(String name)
    {
        this.name = name;
    }

    public Criterion(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Criterion)
        {
            Criterion obj = (Criterion) o;
            return (obj.getId() == null && id == null ||
                    obj.getId() != null && obj.getId().equals(id))
                    &&
                    (obj.getName() == null && name == null ||
                    obj.getName() != null && obj.getName().equals(name));
        }

        return false;
    }
}
