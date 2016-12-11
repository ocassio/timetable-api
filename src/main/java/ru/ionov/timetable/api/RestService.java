package ru.ionov.timetable.api;

import com.google.gson.Gson;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ionov.timetable.api.models.Criterion;
import ru.ionov.timetable.api.models.DateRange;
import ru.ionov.timetable.api.models.Day;
import ru.ionov.timetable.api.providers.DataProvider;
import ru.ionov.timetable.api.utils.DateUtils;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import static spark.Spark.*;

public class RestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestService.class);

    private static final String RESPONSE_TYPE = "application/json";

    private static final String ID_PATH_PARAM = ":id";
    private static final String CRITERIA_TYPE_PARAM = "criteriaType";
    private static final String CRITERION_PARAM = "criterion";
    private static final String FROM_PARAM = "from";
    private static final String TO_PARAM = "to";

    public static void main(String[] args) {

        Gson gson = new Gson();

        port(8080);

        get("/criteria/" + ID_PATH_PARAM, RestService::getCriteria, gson::toJson);
        get("/timetable", RestService::getTimetable, gson::toJson);
    }

    private static List<Criterion> getCriteria(Request request, Response response) {
        response.type(RESPONSE_TYPE);

        try {
            int criteriaType = Integer.parseInt(request.params(ID_PATH_PARAM));
            return DataProvider.getCriteria(criteriaType);
        } catch (NumberFormatException e) {
            LOGGER.error("Wrong criteria type id format", e);
        } catch (IOException e) {
            LOGGER.error("Problem with criteria retrieval", e);
        }

        return Collections.emptyList();
    }

    private static List<Day> getTimetable(Request request, Response response) {

        response.type(RESPONSE_TYPE);

        int criteriaType;

        try {
            criteriaType = Integer.parseInt(request.queryParams(CRITERIA_TYPE_PARAM));
        } catch (NumberFormatException e) {
            LOGGER.error("Wrong criteria type id format", e);
            return Collections.emptyList();
        }

        String criterion = request.queryParams(CRITERION_PARAM);

        if (criterion == null || criterion.isEmpty()) {
            return Collections.emptyList();
        }

        String from = request.queryParams(FROM_PARAM);
        String to = request.queryParams(TO_PARAM);

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
            return DataProvider.getTimetable(criteriaType, criterion, dateRange.getFrom(), dateRange.getTo());
        } catch (IOException e) {
            LOGGER.error("Problem with timetable retrieval", e);
            return Collections.emptyList();
        }
    }

}