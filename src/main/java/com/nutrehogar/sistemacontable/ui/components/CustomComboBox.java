package com.nutrehogar.sistemacontable.ui.components;

import com.nutrehogar.sistemacontable.domain.AccountType;
import com.nutrehogar.sistemacontable.domain.model.Account;
import com.nutrehogar.sistemacontable.domain.model.AccountSubtype;

import javax.swing.*;

/**
 * Combobox personalizado, se usa para definir el tipo de renderizado que tendra cada tipo de dato.
 * <p>
 * Los ya definidos son:
 * <u>
 * <li>{@link AccountType}</li>
 * <li>{@link AccountSubtype}</li>
 * <li>{@link Account}</li>
 * </u>
 *
 * @param <E>
 * @author Calcifer1331
 */
public class CustomComboBox<E> extends JComboBox<E> {
    {
        setRenderer(new CustomListCellRenderer());
    }

    public CustomComboBox() {
    }

//    public CustomComboBox(ComboBoxModel<E> aModel) {
//        super(aModel);
//    }

}
