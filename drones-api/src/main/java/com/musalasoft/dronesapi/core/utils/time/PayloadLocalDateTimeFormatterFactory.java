package com.musalasoft.dronesapi.core.utils.time;

import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PayloadLocalDateTimeFormatterFactory {
    private static PayloadLocalDateTimeFormatterFactory payloadLocalDateTimeFormatterFactory;

    private ConcurrentMap<String, DateTimeFormatter> dateTimeFormatterConcurrentMap = null;

    private PayloadLocalDateTimeFormatterFactory() {
        dateTimeFormatterConcurrentMap = new ConcurrentHashMap<>();
    }

    public static PayloadLocalDateTimeFormatterFactory getInstance() {
        if (ObjectUtils.isEmpty(payloadLocalDateTimeFormatterFactory)) {
            payloadLocalDateTimeFormatterFactory = new PayloadLocalDateTimeFormatterFactory();
        }
        return payloadLocalDateTimeFormatterFactory;
    }

    public String parseLocalDateTimeToString(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(resolveDateTimeFormatterPatter(pattern));
    }

    public LocalDateTime parseStringToLocalDateTime(String dateTimeString, String pattern) {
        return LocalDateTime.parse(dateTimeString, resolveDateTimeFormatterPatter(pattern));
    }

    private DateTimeFormatter resolveDateTimeFormatterPatter(String pattern) {
        DateTimeFormatter dateTimeFormatter = null;
        if (!dateTimeFormatterConcurrentMap.containsKey(pattern)) {
            dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            dateTimeFormatterConcurrentMap.put(pattern, dateTimeFormatter);
        }
        return (!ObjectUtils.isEmpty(dateTimeFormatter) ? dateTimeFormatter : dateTimeFormatterConcurrentMap.get(pattern));
    }
}
