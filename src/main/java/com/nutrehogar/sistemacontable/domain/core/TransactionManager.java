package com.nutrehogar.sistemacontable.domain.core;

import com.nutrehogar.sistemacontable.exception.RepositoryException;
import com.nutrehogar.sistemacontable.infrastructure.persistence.HibernateUtil;
import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class TransactionManager {

    public static <R> R executeInTransaction(Function<Session, R> operation) throws RepositoryException {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession(); // Obtener sesión del pool
            transaction = session.beginTransaction();

            R result = operation.apply(session); // Pasar la sesión a la operación
            transaction.commit();
            return result;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RepositoryException("Error al obtener sesión", e);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            handleException(e); // Manejar excepciones específicas
            throw new RepositoryException("Error en transacción", e);
        } finally {
            if (session != null) {
                HibernateUtil.returnSession(session); // Devolver sesión al pool
            }
        }
    }
    /*
    public class TransactionManager {
    public static <T> T execute(Supplier<T> operation) {
        try (Session session = SessionPool.getSession()) {
            return session.getTransaction().execute(s -> {
                try {
                    return operation.get();
                } catch (Exception e) {
                    throw new RepositoryException(e);
                }
            });
        }
    }
}
     */

    private static void handleException(Exception e) {
        String message = switch (e) {
            case EntityExistsException ex -> "Registro ya existe: " + ex.getMessage();
            case ConstraintViolationException ex when Objects.equals(ex.getConstraintName(), "UK_ACCOUNT_CODE") ->
                    "Código de cuenta duplicado";
            case null -> "Error desconocido";
            default -> "Error: " + e.getMessage();
        };
        System.out.println(message);

    }
}