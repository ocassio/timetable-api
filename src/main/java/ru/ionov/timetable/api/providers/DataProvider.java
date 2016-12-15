package ru.ionov.timetable.api.providers;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.ionov.timetable.api.models.Criterion;
import ru.ionov.timetable.api.models.DateRange;
import ru.ionov.timetable.api.models.Day;
import ru.ionov.timetable.api.models.Lesson;
import ru.ionov.timetable.api.models.TimeRange;
import ru.ionov.timetable.api.utils.DateUtils;

public final class DataProvider
{
    private static final String TIMETABLE_URL = "http://www.tolgas.ru/services/raspisanie/";
    private static final String POST_DATA_CHARSET = "windows-1251";
    private static final int CONNECTION_TIMEOUT = 15000;

    private static final String ATTR_VALUE = "value";

    private static final int COL_COUNT = 7;

    private static final List<TimeRange> TIME_RANGES = new ArrayList<TimeRange>()
    {
        {
            add(new TimeRange("09:00", "10:35"));
            add(new TimeRange("10:45", "12:20"));
            add(new TimeRange("13:00", "14:35"));
            add(new TimeRange("14:45", "16:20"));
            add(new TimeRange("16:30", "18:05"));
            add(new TimeRange("18:15", "19:50"));
            add(new TimeRange("20:00", "21:35"));
        }
    };

    private static final List<TimeRange> TIME_RANGES_SATURDAY = new ArrayList<TimeRange>()
    {
        {
            add(new TimeRange("08:30", "10:05"));
            add(new TimeRange("10:15", "11:50"));
            add(new TimeRange("12:35", "14:10"));
            add(new TimeRange("14:20", "15:55"));
            add(new TimeRange("16:05", "17:35"));
        }
    };

    private DataProvider() {}

    public static List<Criterion> getCriteria(int criteriaType) throws IOException
    {
        List<Criterion> criteria = new ArrayList<>();

        Document document = Jsoup.connect(TIMETABLE_URL)
                .timeout(CONNECTION_TIMEOUT)
                .method(Connection.Method.GET)
                .data("id", Integer.toString(criteriaType))
                .get();

        Elements elements = document.select("#vr option");

        for (Element element : elements)
        {
            criteria.add(new Criterion(element.attr(ATTR_VALUE), element.text()));
        }

        return criteria;
    }

    public static List<Day> getTimetable(int criteriaType, String criterion, Date from, Date to) throws IOException
    {
        Document document = Jsoup.connect(TIMETABLE_URL)
                .postDataCharset(POST_DATA_CHARSET)
                .timeout(CONNECTION_TIMEOUT)
                .method(Connection.Method.POST)
                .data("rel", Integer.toString(criteriaType))
                .data("vr", criterion)
                .data("from", DateUtils.toDateString(from))
                .data("to", DateUtils.toDateString(to))
                .data("submit_button", "ПОКАЗАТЬ")
                .post();

        Elements elements = document.select("#send td.hours");
        if (elements.isEmpty())
        {
            throw new IOException("Timetable is missing on loaded page");
        }

        return getDays(elements);
    }

    private static List<Day> getDays(Elements elements)
    {
        List<Day> days = new ArrayList<>();

        // No lessons have been found
        if (elements.size() == 1)
        {
            return days;
        }

        int i = 0;
        while (i < elements.size())
        {
            String date = elements.get(i).text();
            if (DateUtils.isDate(date))
            {
                Day day = new Day(date);
                try {
                    day.setDayOfWeek(DateUtils.getDayOfWeekName(DateUtils.toDate(date)));
                } catch (ParseException ignored) {}

                i++;
                do
                {
                    List<String> params = new ArrayList<>();

                    for (int j = 0; j < COL_COUNT; j++)
                    {
                        params.add(elements.get(i).text());
                        i++;
                    }

                    Lesson lesson = new Lesson(params);
                    setTimeRanges(date, lesson);
                    day.addLesson( lesson);
                }
                while (i < elements.size() && !DateUtils.isDate(elements.get(i).text()));

                days.add(day);
            }
        }

        return days;
    }

    private static void setTimeRanges(String stringDate, Lesson lesson)
    {
        try
        {
            Date date = DateUtils.toDate(stringDate);
            int lessonNum = Integer.parseInt(lesson.getNumber());
            if (!DateUtils.equalsDayOfWeek(date, Calendar.SATURDAY))
            {
                if (lessonNum <= TIME_RANGES.size())
                {
                    lesson.setTime(TIME_RANGES.get(lessonNum - 1));
                }
            }
            else
            {
                if (lessonNum <= TIME_RANGES_SATURDAY.size())
                {
                    lesson.setTime(TIME_RANGES_SATURDAY.get(lessonNum - 1));
                }
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
}
