package com.nutrehogar.sistemacontable.domain.repository;

import com.nutrehogar.sistemacontable.domain.HibernateUtil;
import com.nutrehogar.sistemacontable.domain.model.Registro;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class RegistroRepo {
    private static final Session session = HibernateUtil.getSession();

    public static List<Registro> findAll() {
        List<Registro> transaccions = List.of();
        try {
            session.beginTransaction();
            transaccions = session.createQuery("from Registro", Registro.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
        return transaccions;
    }

    public static Registro findById(Integer id) {
        return session.find(Registro.class, id);
    }

    public static void save(Registro Registro) {
        try {
            session.beginTransaction();
            session.persist(Registro);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    public static void save(@NotNull List<Registro> registros) {
        try {
            session.beginTransaction();
            registros.forEach(session::persist);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    public static void delete(Registro registro) {
        try {
            session.beginTransaction();

            session.remove(session.contains(registro) ? registro : session.merge(registro));

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public static void delete(Integer id) {
        try {
            session.beginTransaction();

            Optional.ofNullable(findById(id)).ifPresent(session::remove);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public static void update(Registro registro) {
        try {
            session.beginTransaction();
            session.merge(registro);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}