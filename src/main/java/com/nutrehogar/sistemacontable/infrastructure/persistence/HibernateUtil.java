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

    private static final SessionFactory sessionFactory = buildSessionFactory(); // Instancia de SessionFactory
    private static Session session = null; // Instancia única de Session

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

            configuration.setProperty("hibernate.connection.url", "jdbc:sqlite:" + ConfigLoader.Props.DB_NAME.getPath().toString());

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

    /**
     * Obtiene la sesión de Hibernate.
     * Si no hay una sesión activa, se crea una nueva.
     *
     * @return la sesión activa de Hibernate
     */
    public static synchronized Session getSession() {
        if (session == null || !session.isOpen()) {
            session = sessionFactory.openSession(); // Crea una nueva sesión si es necesario
            log.info("Open session");
        }
        return session; // Devuelve la sesión activa
    }


    /**
     * Cierra la sesión de Hibernate y el SessionFactory.
     * Debe ser llamado al finalizar el uso de la aplicación.
     */
    public static synchronized void shutdown() {
        try {
            log.info("Iniciando el proceso de cierre...");

            // Esperar si hay tareas en ejecución antes de cerrar la sesión
            if (session != null && session.isOpen()) {
                log.info("Cerrando la sesión activa...");
                session.close();
            }

            // Asegurar que no haya procesos en ejecución antes de cerrar la fábrica de sesiones
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                log.info("Cerrando SessionFactory...");
                sessionFactory.close();
            }

            // Si tienes un pool de hilos, asegurarte de cerrarlo de manera segura
//            if (executorService != null && !executorService.isShutdown()) {
//                log.info("Esperando que todas las tareas finalicen...");
//                executorService.shutdown();
//                try {
//                    if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
//                        log.warn("Forzando la detención de tareas en ejecución...");
//                        executorService.shutdownNow();
//                    }
//                } catch (InterruptedException e) {
//                    log.error("Error al esperar la terminación de tareas", e);
//                    executorService.shutdownNow();
//                    Thread.currentThread().interrupt();
//                }
//            }

            log.info("Cierre seguro completado.");
        } catch (Exception e) {
            log.error("Error durante el proceso de cierre", e);
        }
    }

}
