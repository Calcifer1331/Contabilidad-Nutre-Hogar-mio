package com.nutrehogar.sistemacontable.domain.repository;

import com.nutrehogar.sistemacontable.application.repository.LedgerRecordRepository;
import com.nutrehogar.sistemacontable.domain.core.CRUDRepositoryImpl;
import com.nutrehogar.sistemacontable.domain.core.TransactionManager;
import com.nutrehogar.sistemacontable.domain.model.Account;
import com.nutrehogar.sistemacontable.domain.model.JournalEntry;
import com.nutrehogar.sistemacontable.domain.model.LedgerRecord;
import com.nutrehogar.sistemacontable.exception.RepositoryException;

import java.time.LocalDate;
import java.util.List;

public class LedgerRecordRepo extends CRUDRepositoryImpl<LedgerRecord, Integer> implements LedgerRecordRepository {
    public LedgerRecordRepo() {
        super(LedgerRecord.class);
    }

    @Override
    public List<LedgerRecord> findByDateRangeAndAccount(Account account, LocalDate startDate, LocalDate endDate) throws RepositoryException {
        return TransactionManager.executeInTransaction(session -> session.createQuery(
                        "SELECT lr FROM LedgerRecord lr " +
                                "JOIN FETCH lr.journalEntry " +  // 🔹 Forzar la carga de journalEntry
                                "WHERE lr.account = :account " +
                                "AND lr.journalEntry.date BETWEEN :startDate AND :endDate " +
                                "ORDER BY lr.journalEntry.date DESC", LedgerRecord.class
                )
                .setParameter("account", account)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .list());

    }
}
