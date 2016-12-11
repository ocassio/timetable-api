package ru.ionov.timetable.api.models;

import java.util.Date;

import ru.ionov.timetable.api.utils.DateUtils;

public class DateRange
{
    private Date from;
    private Date to;

    public DateRange()
    {
    }

    public DateRange(Date from, Date to)
    {
        this.from = from;
        this.to = to;
    }

    public Date getFrom()
    {
        return from;
    }

    public void setFrom(Date from)
    {
        if (from != null && to != null && from.after(to))
        {
            to = from;
        }
        this.from = from;
    }

    public Date getTo()
    {
        return to;
    }

    public void setTo(Date to)
    {
        if (from != null && to != null && from.after(to))
        {
            from = to;
        }
        this.to = to;
    }

    @Override
    public String toString()
    {
        return DateUtils.toDateString(from) + " - " + DateUtils.toDateString(to);
    }
}
