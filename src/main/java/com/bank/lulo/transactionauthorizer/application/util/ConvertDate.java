package com.bank.lulo.transactionauthorizer.application.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConvertDate {

    private static final String FORMAT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static LocalDateTime getLocalDateTimeFromString(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_ISO_8601);
        return LocalDateTime.parse(date, formatter);
    }
}
