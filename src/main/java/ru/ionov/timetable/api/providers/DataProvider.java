package ru.ionov.timetable.api.providers;

import ru.ionov.timetable.api.models.Criterion;
import ru.ionov.timetable.api.models.Day;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface DataProvider {
    List<Criterion> getCriteria(int criteriaType) throws IOException;
    List<Day> getTimetable(int criteriaType, String criterion, Date from, Date to) throws IOException;
    void evictCache();
}
