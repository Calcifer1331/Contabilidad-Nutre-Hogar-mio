package com.nutrehogar.sistemacontable.domain.repository;

import com.nutrehogar.sistemacontable.application.repository.BackupRepository;
import com.nutrehogar.sistemacontable.domain.core.WriteExecutor;
import com.nutrehogar.sistemacontable.exception.RepositoryException;
import com.nutrehogar.sistemacontable.infrastructure.persistence.HibernateUtil;
import org.hibernate.Session;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

public class BackupRepo implements BackupRepository {
    @Override
    public void backup(String fileName) throws RepositoryException {
        try {
            execute(stmt -> {
                try {
                    stmt.execute("VACUUM INTO '" + fileName + "';");
                } catch (SQLException e) {
                    throw new RepositoryException(e);
                }
            });
        } catch (InterruptedException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public void restore(String filePath) throws RepositoryException {
        try {
            execute(stmt -> {
                // Elimina los datos actuales de las tablas
                try {
                    stmt.execute("DELETE FROM main.account_subtype;");
                    stmt.execute("DELETE FROM main.account;");
                    stmt.execute("DELETE FROM main.ledger_record;");
                    stmt.execute("DELETE FROM main.journal_entry;");
                    stmt.execute("DELETE FROM main.user;");

                    // Adjunta la base de datos de respaldo
                    stmt.execute("ATTACH DATABASE '" + filePath + "' AS BACKUP;");

                    // Copia los datos del respaldo a las tablas principales
                    stmt.execute("INSERT INTO main.account_subtype SELECT * FROM BACKUP.account_subtype;");
                    stmt.execute("INSERT INTO main.account SELECT * FROM BACKUP.account;");
                    stmt.execute("INSERT INTO main.ledger_record SELECT * FROM BACKUP.ledger_record;");
                    stmt.execute("INSERT INTO main.journal_entry SELECT * FROM BACKUP.journal_entry;");
                    stmt.execute("INSERT INTO main.user SELECT * FROM BACKUP.user;");
                } catch (SQLException e) {
                    throw new RepositoryException(e);
                }
            });
        } catch (InterruptedException e) {
            throw new RepositoryException(e);
        }
    }

    public void execute(Consumer<Statement> consumer) throws InterruptedException {
//        WriteExecutor.submitWrite(() -> {
            Session session = null;
            try {
                session = HibernateUtil.getSession();
                session.doWork(connection -> {
                    try (Statement stmt = connection.createStatement()) {
                        connection.setAutoCommit(true);
                        consumer.accept(stmt);
                        connection.setAutoCommit(false);
                    }
                });
            } finally {
                if (session != null) {
                    HibernateUtil.returnSession(session); // Devolver sesión al pool
                }
            }
//            return null;
//        });
    }
}
