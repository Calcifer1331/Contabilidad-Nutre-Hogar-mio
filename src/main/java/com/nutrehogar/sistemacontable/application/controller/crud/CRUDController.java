package com.nutrehogar.sistemacontable.application.controller.crud;

import com.nutrehogar.sistemacontable.application.controller.SimpleController;
import com.nutrehogar.sistemacontable.application.repository.crud.CRUDRepository;
import com.nutrehogar.sistemacontable.domain.model.AuditableEntity;
import com.nutrehogar.sistemacontable.exception.RepositoryException;
import com.nutrehogar.sistemacontable.ui.view.crud.CRUDView;
import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectDeletedException;
import org.hibernate.exception.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

@Slf4j
public abstract class CRUDController<T, ID> extends SimpleController<T> {
    public CRUDController(CRUDRepository<T, ID> repository, CRUDView view) {
        super(repository, view);
        initPopupMenu();
    }

    @Override
    protected void loadData() {
        setData(getRepository().findAll());
        super.loadData();
    }

    private final JPopupMenu popupMenu = new JPopupMenu();
    private final JMenuItem detailsItem = new JMenuItem("Ver detalles de auditoría");

    private void initPopupMenu() {
        detailsItem.addActionListener(evt -> showAuditDetails());
        popupMenu.add(detailsItem);
    }


    private void showAuditDetails() {
        if (getSelected() != null && getSelected() instanceof AuditableEntity e) {
            String message = "📌 Creado por: " + e.getCreatedBy() + "\n" +
                    "🕒 Fecha creación: " + e.getCreatedAt() + "\n" +
                    "✏️ Actualizado por: " + e.getUpdatedBy() + "\n" +
                    "🕒 Fecha actualización: " + e.getUpdatedAt();
            JOptionPane.showMessageDialog(getView(), message, "Detalles de Auditoría", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    protected void setupViewListeners() {
        getBtnSave().addActionListener(e -> save(prepareToSave()));
        getBtnDelete().addActionListener(e -> delete(prepareToDelete()));
        getBtnUpdate().addActionListener(e -> update(prepareToUpdate()));
        getBtnEdit().addActionListener(e -> prepareToEdit());
        getBtnAdd().addActionListener(e -> prepareToAdd());
        deselect();
        super.setupViewListeners();
    }

    private void save(T entity) {
        if (entity == null) return;
        try {
            getRepository().save(entity);
            loadData(); // Recargar datos después de guardar
            prepareToAdd();
        } catch (RepositoryException e) {
            String fullMessage = switch (e.getCause()) {
                case EntityExistsException c -> "Ya existe esa Cuenta";
                case IllegalArgumentException c -> "Los datos no puede ser nulo";
                case ConstraintViolationException c -> "Codigo de cuenta duplicado";
                case null, default -> e.getMessage();
            };
            showError("Error al guardar: " + fullMessage);
        }
    }


    private void update(T entity) {
        if (entity == null) return;
        try {
            getRepository().update(getSelected());
            loadData(); // Recargar datos después de eliminar
            prepareToAdd();
        } catch (RepositoryException e) {
            String fullMessage = switch (e.getCause()) {
                case IllegalArgumentException c -> "Los datos no son validos";
                case ObjectDeletedException c -> "No se puede editar una cuenta eliminada";
                case ConstraintViolationException c -> "Operación no valido";
                case null, default -> e.getMessage();
            };
            showError("Error al guardar: " + fullMessage);
        }
    }


    private void delete(ID id) {
        if (id == null) {
            prepareToAdd();
            return;
        }
        var response = JOptionPane.showConfirmDialog(getView(),
                "Desea eliminar? El cambio sera permanente.",
                "Elimination",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (response != JOptionPane.OK_OPTION) return;
        try {
            getRepository().deleteById(id);
            loadData(); // Recargar datos después de eliminar
            prepareToAdd();
        } catch (RepositoryException e) {
            showError("Error al guardar: " + e.getMessage());
        }
    }

    protected void deselect() {
        setSelected(null);
        getBtnDelete().setEnabled(false);
        getBtnEdit().setEnabled(false);
    }

    protected void select() {
        getBtnDelete().setEnabled(true);
        getBtnEdit().setEnabled(true);
    }

    @Override
    public void setSelected(T selected) {
        super.setSelected(selected);
        select();
    }

    @Override
    protected void setElementSelected(@NotNull MouseEvent e) {
        int row = getTblData().rowAtPoint(e.getPoint());
        if (row != -1) {
            int selectedRow = getTblData().getSelectedRow();
            if (selectedRow < 0) {
                deselect();
                return;
            }
            setSelected(getData().get(selectedRow));
            popupMenu.show(getTblData(), e.getX(), e.getY());
        }
    }

    protected void prepareToEdit() {
        getBtnUpdate().setEnabled(true);
        getBtnSave().setEnabled(false);
    }

    protected void prepareToAdd() {
        deselect();
        getBtnUpdate().setEnabled(false);
        getBtnSave().setEnabled(true);
    }

    protected abstract ID prepareToDelete();

    protected abstract T prepareToSave();

    protected abstract T prepareToUpdate();

    @Override
    public CRUDRepository<T, ID> getRepository() {
        return (CRUDRepository<T, ID>) super.getRepository();
    }

    @Override
    public CRUDView getView() {
        return (CRUDView) super.getView();
    }

    public JButton getBtnAdd() {
        return getView().getBtnAdd();
    }

    public JButton getBtnUpdate() {
        return getView().getBtnUpdate();
    }

    public JButton getBtnEdit() {
        return getView().getBtnEdit();
    }

    public JButton getBtnDelete() {
        return getView().getBtnDelete();
    }

    public JButton getBtnSave() {
        return getView().getBtnSave();
    }
}
