package com.nutrehogar.sistemacontable.application.repository;

import com.nutrehogar.sistemacontable.domain.model.Account;
import com.nutrehogar.sistemacontable.domain.model.LedgerRecord;
import com.nutrehogar.sistemacontable.exception.RepositoryException;

import java.time.LocalDate;
import java.util.List;

public interface LedgerRecordRepository extends CRUDRepository<LedgerRecord, Integer> {
    List<LedgerRecord> findByDateRangeAndAccount(Account account, LocalDate startDate, LocalDate endDate) throws RepositoryException;
}
