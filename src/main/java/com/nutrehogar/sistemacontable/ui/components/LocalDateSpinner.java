package com.nutrehogar.sistemacontable.ui.components;


import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Spinner que tiene como valor un {@link LocalDate}, para esto tiene un {@link LocalDateSpinnerModel}.
 *
 * @author Calcifer1331
 * @see LocalDateSpinnerModel
 */
public class LocalDateSpinner extends JSpinner {
    {
        configEditor();
    }

    public LocalDateSpinner() {
        super(new LocalDateSpinnerModel());
    }

    public LocalDateSpinner(LocalDateSpinnerModel localDateSpinnerModel) {
        super(localDateSpinnerModel);
    }

    @Override
    public LocalDateSpinnerModel getModel() {
        return (LocalDateSpinnerModel) super.getModel();
    }

    @Override
    public LocalDate getValue() {
        return (LocalDate) super.getValue();
    }

    /**
     * Le da la funcionalidad al espiner de aumentar o decrementar la fecha dependiendo de que seccion se la seleccionada
     */
    public final void configEditor() {
        DefaultEditor editor = new DefaultEditor(this);
        this.setEditor(editor);
        editor.getTextField().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int position = editor.getTextField().getCaretPosition();
                if (position < 5) {
                    getModel().setIncrementUnit(ChronoUnit.YEARS);
                } else if (position < 8) {
                    getModel().setIncrementUnit(ChronoUnit.MONTHS);
                } else {
                    getModel().setIncrementUnit(ChronoUnit.DAYS);
                }
            }
        });
    }
}
