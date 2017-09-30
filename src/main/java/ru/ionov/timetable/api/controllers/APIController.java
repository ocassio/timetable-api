package ru.ionov.timetable.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ionov.timetable.api.exceptions.DataRetrievalException;
import ru.ionov.timetable.api.models.Criterion;
import ru.ionov.timetable.api.models.DateRange;
import ru.ionov.timetable.api.models.Day;
import ru.ionov.timetable.api.providers.DataProvider;
import ru.ionov.timetable.api.utils.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

@RestController
public class APIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(APIController.class);

    private final DataProvider dataProvider;

    @Autowired
    public APIController(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @RequestMapping("/criteria/{id}")
    public List<Criterion> getCriteria(@PathVariable("id") int criteriaType) {
        try {
            return dataProvider.getCriteria(criteriaType);
        } catch (NumberFormatException e) {
            throw new DataRetrievalException("Wrong criteria type id format", e);
        } catch (IOException e) {
            throw new DataRetrievalException("Problem with criteria retrieval", e);
        }
    }

    @RequestMapping("/timetable")
    public List<Day> getTimetable(
            @RequestParam("criteriaType") int criteriaType,
            @RequestParam("criterion") String criterion,
            @RequestParam(name = "from", required = false) String from,
            @RequestParam(name = "to", required = false) String to) {

        if (criterion == null || criterion.isEmpty()) {
            return Collections.emptyList();
        }

        DateRange dateRange = DateUtils.getSevenDays();

        if (DateUtils.isDate(from)) {
            if (DateUtils.isDate(to)) {
                try {
                    dateRange = new DateRange();
                    dateRange.setFrom(DateUtils.toDate(from));
                    dateRange.setTo(DateUtils.toDate(to));
                } catch (ParseException e) {
                    LOGGER.warn("Wrong date range", e);
                }
            } else {
                try {
                    dateRange = DateUtils.getSevenDays(DateUtils.toDate(from));
                } catch (ParseException e) {
                    LOGGER.warn("Wrong date range", e);
                }
            }
        }

        try {
            return dataProvider.getTimetable(criteriaType, criterion, dateRange.getFrom(), dateRange.getTo());
        } catch (IOException e) {
            throw new DataRetrievalException("Problem with timetable retrieval", e);
        }
    }

}