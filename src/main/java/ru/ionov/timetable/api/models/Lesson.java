package ru.ionov.timetable.api.models;

import java.io.Serializable;
import java.util.List;

public class Lesson implements Serializable
{
    private String number;
    private String room;
    private String name;
    private String teacher;
    private String type;
    private String group;
    private String note;

    private TimeRange time;

    public Lesson()
    {
    }

    public Lesson(String number, String room, String name, String teacher, String type, String group, String note, TimeRange time)
    {
        this.number = number;
        this.room = room;
        this.name = name;
        this.teacher = teacher;
        this.type = type;
        this.group = group;
        this.time = time;
        this.note = note;
    }

    public Lesson(String[] params)
    {
        this.room = params[0];
        this.number = params[1];
        this.teacher = params[2];
        this.type = params[3];
        this.name = params[4];
        this.group = params[5];
        this.note = params[6];
    }

    public Lesson(List<String> params)
    {
        this(params.toArray(new String[params.size()]));
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getRoom()
    {
        return room;
    }

    public void setRoom(String room)
    {
        this.room = room;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTeacher()
    {
        return teacher;
    }

    public void setTeacher(String teacher)
    {
        this.teacher = teacher;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getGroup()
    {
        return group;
    }

    public void setGroup(String group)
    {
        this.group = group;
    }

    public TimeRange getTime()
    {
        return time;
    }

    public void setTime(TimeRange time)
    {
        this.time = time;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public boolean contains(String query)
    {
        return (isNonNullAndContains(room, query) || isNonNullAndContains(name, query)  ||
                isNonNullAndContains(teacher, query) || isNonNullAndContains(type, query)  ||
                isNonNullAndContains(group, query) || isNonNullAndContains(note, query));
    }

    private boolean isNonNullAndContains(String target, String value)
    {
        return (target != null && target.toLowerCase().contains(value));
    }

    @Override
    public String toString()
    {
        return number + ", " + room + ", " + name + ", " + type + ", " + teacher;
    }
}
