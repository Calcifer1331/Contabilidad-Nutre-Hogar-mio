package com.nutrehogar.sistemacontable.domain.repository;

import com.nutrehogar.sistemacontable.application.repository.crud.JournalEntryRepository;
import com.nutrehogar.sistemacontable.domain.core.CRUDRepositoryImpl;
import com.nutrehogar.sistemacontable.domain.model.JournalEntry;
import com.nutrehogar.sistemacontable.exception.RepositoryException;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class JournalEntryRepositoryImpl extends CRUDRepositoryImpl<JournalEntry, Integer>
        implements JournalEntryRepository {

    public JournalEntryRepositoryImpl(Session session) {
        super(JournalEntry.class, session);
    }

    @Override
    public List<JournalEntry> findAllByDateRange(LocalDate startDate, LocalDate endDate) throws RepositoryException {
        return executeInTransaction(() ->
                getSession().createQuery(
                                "FROM JournalEntry WHERE date BETWEEN :startDate AND :endDate ORDER BY date",
                                JournalEntry.class
                        )
                        .setParameter("startDate", startDate)
                        .setParameter("endDate", endDate)
                        .list()
        );
    }
}