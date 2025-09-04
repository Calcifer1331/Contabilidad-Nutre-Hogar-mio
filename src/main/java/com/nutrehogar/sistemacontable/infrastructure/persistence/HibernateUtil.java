package com.nutrehogar.sistemacontable.infrastructure.persistence;

import com.nutrehogar.sistemacontable.application.config.ConfigLoader;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;

/**
 * HibernateUtil es una clase de utilidad que gestiona la configuración de Hibernate
 * y proporciona una única instancia de SessionFactory y Session a lo largo de la vida de la aplicación.
 * Esta clase utiliza el patrón Singleton para asegurar que solo haya una sesión de Hibernate activa
 * en la aplicación de escritorio.
 *
 * <p>
 * Se recomienda cerrar la sesión y el SessionFactory al finalizar el uso de la aplicación.
 * </p>
 */
@Slf4j
public class HibernateUtil {
    private HibernateUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static final SessionPool sessionPool = new SessionPool(sessionFactory, 3); // Pool de 5 sesiones

    /**
     * Construye el SessionFactory utilizando la configuración especificada en hibernate.cfg.xml.
     *
     * @return SessionFactory construida
     * @throws ExceptionInInitializerError si la configuración falla
     */
    private static SessionFactory buildSessionFactory() {
        log.info("Building Hibernate SessionFactory");
        try {
            // Cargar la configuración de hibernate.cfg.xml
            Configuration configuration = new Configuration().configure();
            configuration.setProperty("hibernate.connection.url", "jdbc:sqlite:" + ConfigLoader.Props.DB_NAME.getPath().toString() + "?journal_mode=WAL");

            return configuration.buildSessionFactory();
        } catch (Exception e) {
            log.error("Error building SessionFactory", e);

            JOptionPane.showMessageDialog(null,
                    "Error al iniciar la sesión de Hibernate: " + e.getMessage(),
                    "Error de Configuración",
                    JOptionPane.ERROR_MESSAGE);

            System.exit(1);
            return null;
        }
    }

    public static Session getSession() throws InterruptedException {
        return sessionPool.borrowSession();
    }

    public static void returnSession(Session session) {
        sessionPool.returnSession(session);
    }

    public static void shutdown() {
        sessionPool.shutdown();
        sessionFactory.close();
    }

}
