package ru.ionov.timetable.api.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Day implements Serializable
{
    private String date;
    private String dayOfWeek;
    private List<Lesson> lessons;

    public Day()
    {
    }

    public Day(String date)
    {
        this.date = date;
    }

    public Day(String date, String dayOfWeek) {
        this.date = date;
        this.dayOfWeek = dayOfWeek;
    }

    public Day(String date, String dayOfWeek, List<Lesson> lessons) {
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.lessons = lessons;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public List<Lesson> getLessons()
    {
        if (lessons == null)
        {
            lessons = new ArrayList<>();
        }

        return lessons;
    }

    public void setLessons(List<Lesson> lessons)
    {
        this.lessons = lessons;
    }

    public void addLesson(Lesson lesson)
    {
        getLessons().add(lesson);
    }

    public boolean contains(String query)
    {
        if (this.getDate().toLowerCase().contains(query)) return true;

        for (Lesson lesson : lessons)
        {
            if (lesson.contains(query))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append(date);
        buffer.append("\n");
        for (Lesson lesson : lessons)
        {
            buffer.append(lesson.toString());
            buffer.append("\n");
        }

        return buffer.toString();
    }
}
