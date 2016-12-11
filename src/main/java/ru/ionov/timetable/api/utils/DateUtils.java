package ru.ionov.timetable.api.utils;

import ru.ionov.timetable.api.models.DateRange;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtils
{
    private static final Locale LOCALE = new Locale("ru", "RU");

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm", LOCALE);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", LOCALE);

    private DateUtils() {}

    public static String toDateString(Date date)
    {
        return DATE_FORMAT.format(date);
    }

    public static String toTimeString(Date date)
    {
        return TIME_FORMAT.format(date);
    }

    public static Date toDate(String string) throws ParseException
    {
        return DATE_FORMAT.parse(string);
    }

    public static boolean isDate(String value)
    {
        if (value == null || value.isEmpty()) return false;
        try
        {
            DateUtils.toDate(value);
        }
        catch (ParseException e)
        {
            return false;
        }

        return true;
    }

    public static String getDayOfWeekName(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, LOCALE);
    }

    public static boolean equalsDayOfWeek(Date date, int dayOfWeek)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek;
    }

    public static DateRange getDefaultDateRange()
    {
        Date currentDate = Calendar.getInstance().getTime();
        return new DateRange(currentDate, currentDate);
    }

    public static DateRange getSevenDays() {
        return getSevenDays(null);
    }

    public static DateRange getSevenDays(Date from)
    {
        DateRange dateRange = new DateRange();
        Calendar calendar = Calendar.getInstance();

        if (from != null) {
            calendar.setTime(from);
        }

        dateRange.setFrom(calendar.getTime());

        calendar.add(Calendar.DAY_OF_MONTH, 6);
        dateRange.setTo(calendar.getTime());

        return dateRange;
    }

    public static DateRange getCurrentWeek()
    {
        DateRange dateRange = new DateRange();
        Calendar calendar = Calendar.getInstance(LOCALE);

        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        dateRange.setFrom(calendar.getTime());

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        dateRange.setTo(calendar.getTime());

        return dateRange;
    }

    public static DateRange getNextWeek()
    {
        DateRange dateRange = new DateRange();
        Calendar calendar = Calendar.getInstance(LOCALE);
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        dateRange.setFrom(calendar.getTime());

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        dateRange.setTo(calendar.getTime());

        return dateRange;
    }

    public static DateRange getCurrentMonth()
    {
        DateRange dateRange = new DateRange();
        Calendar calendar = Calendar.getInstance(LOCALE);

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        dateRange.setFrom(calendar.getTime());

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        dateRange.setTo(calendar.getTime());

        return dateRange;
    }

    public static boolean isToday(Date date)
    {
        if (date == null)
        {
            return false;
        }

        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return (now.get(Calendar.ERA) == calendar.get(Calendar.ERA) &&
                now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR));
    }
}
