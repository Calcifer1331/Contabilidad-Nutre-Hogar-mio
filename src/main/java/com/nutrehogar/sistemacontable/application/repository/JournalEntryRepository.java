package com.nutrehogar.sistemacontable.application.repository;

import com.nutrehogar.sistemacontable.domain.model.JournalEntry;
import com.nutrehogar.sistemacontable.domain.model.JournalEntryPK;
import com.nutrehogar.sistemacontable.exception.RepositoryException;

import java.time.LocalDate;
import java.util.List;

public interface JournalEntryRepository extends CRUDRepository<JournalEntry, JournalEntryPK> {
    List<JournalEntry> findAllByDateRange(LocalDate startDate, LocalDate endDate) throws RepositoryException;
}