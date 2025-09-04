package com.nutrehogar.sistemacontable.ui.components;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;
import java.util.Vector;

public class CustomComboBoxModel<E> extends DefaultComboBoxModel<E> {
    public CustomComboBoxModel(@NotNull List<E> data) {
        super(new Vector<>(data));
        if (!data.isEmpty()) setSelectedItem(data.getFirst());
    }

    public CustomComboBoxModel(@NotNull E[] data) {
        super(data);
        setSelectedItem(data[0]);
    }

    @Override
    public E getSelectedItem() {
        return (E) super.getSelectedItem();
    }

    /**
     * Metodo para agregar los datos a la lista que se mostraran
     *
     * @param data lista de subtipo cuentas a mostrar en el combo box
     */
    public void setData(List<E> data) {
        this.removeAllElements();
        if (data != null) {
            for (E e : data) {
                this.addElement(e);
            }
            setSelectedItem(data.getFirst());
        }
    }
}