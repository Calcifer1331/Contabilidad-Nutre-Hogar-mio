package com.nutrehogar.sistemacontable.application.config;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.function.Function;

public class Util {
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");
    public static final LocalDate CURRENT_DATE = LocalDate.now();
    public static final String NA = "N/A";


    // Convierte cualquier objeto a String de forma segura
    public static String toStringSafe(Object obj) {
        return (obj != null) ? obj.toString() : "";
    }

    // Maneja conversiones seguras con funciones
    public static <T> String toStringSafe(T obj, Function<T, String> extractor) {
        return (obj != null) ? extractor.apply(obj) : "";
    }

    // Maneja conversiones seguras de BigDecimal o Double usando DECIMAL_FORMAT
    public static String formatDecimalSafe(Number number) {
        return (number != null) ? DECIMAL_FORMAT.format(number) : "";
    }

}
