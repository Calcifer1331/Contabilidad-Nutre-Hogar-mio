package com.nutrehogar.sistemacontable.ui.components;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.nutrehogar.sistemacontable.application.config.Util.CURRENT_DATE;


/**
 * Modelo de un spinner que tiene como valor un {@link LocalDate}
 *
 * @author Calcifer1331
 * @see LocalDate
 * @see LocalDateSpinner
 */
public class LocalDateSpinnerModel extends AbstractSpinnerModel {
    /**
     * Valor actual del spinner
     */
    private LocalDate currentDate;
    /**
     * Fecha minima que puede optiene el valor del spinner (opcional)
     */
    @Getter
    private LocalDate minDate;
    /**
     * Fecha maxima que puede optiene el valor del spinner (opcional)
     */
    @Getter
    private LocalDate maxDate;
    /**
     * Unidad en la que aumenta o disminuye la fecha
     */
    @Getter
    @Setter
    private ChronoUnit incrementUnit;

    public LocalDateSpinnerModel() {
        this.currentDate = CURRENT_DATE;
        this.incrementUnit = ChronoUnit.DAYS;
    }

    public LocalDateSpinnerModel(LocalDate currentDate) {
        this.currentDate = ifDateNull(currentDate);
        this.incrementUnit = ChronoUnit.DAYS;
    }

    public LocalDateSpinnerModel(LocalDate initialDate, LocalDate minDate, LocalDate maxDate, ChronoUnit incrementUnit) {
        this.currentDate = ifDateNull(initialDate);
        this.minDate = ifDateNull(minDate);
        this.maxDate = ifDateNull(maxDate);
        this.incrementUnit = incrementUnit == null ? ChronoUnit.DAYS : incrementUnit;
    }

    private LocalDate ifDateNull(LocalDate date) {
        return date == null ? CURRENT_DATE : date;
    }

    @Override
    public LocalDate getValue() {
        return currentDate;
    }

    public void setValue(LocalDate value) {
        currentDate = ifDateNull(value);
        fireStateChanged();
    }

    public void resetValue() {
        currentDate = CURRENT_DATE;
        fireStateChanged();
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof LocalDate newDate) {
            if (!newDate.equals(currentDate)) {
                if (isWithinBounds(newDate)) {
                    currentDate = newDate;
                    fireStateChanged();
                }
            }
        }
    }

    @Override
    public Object getNextValue() {
        LocalDate nextDate = currentDate.plus(1, incrementUnit);
        return isWithinBounds(nextDate) ? nextDate : null;
    }

    @Override
    public Object getPreviousValue() {
        LocalDate previousDate = currentDate.minus(1, incrementUnit);
        return isWithinBounds(previousDate) ? previousDate : null;
    }

    /**
     * Verifica si la fecha es valida
     *
     * @param date fecha a verifica
     * @return
     */
    private boolean isWithinBounds(LocalDate date) {
        boolean afterMin = (minDate == null || !date.isBefore(minDate));
        boolean beforeMax = (maxDate == null || !date.isAfter(maxDate));
        return afterMin && beforeMax;
    }

}

