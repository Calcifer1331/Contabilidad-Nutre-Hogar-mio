package com.nutrehogar.sistemacontable.application.repository.crud;

import com.nutrehogar.sistemacontable.domain.model.JournalEntry;
import com.nutrehogar.sistemacontable.domain.model.LedgerRecord;
import com.nutrehogar.sistemacontable.exception.RepositoryException;

import java.time.LocalDate;
import java.util.List;

public interface LedgerRecordRepository extends CRUDRepository<LedgerRecord, Integer> {
}
