package com.nutrehogar.sistemacontable.domain.repository;

import com.nutrehogar.sistemacontable.application.repository.JournalEntryRepository;
import com.nutrehogar.sistemacontable.infrastructure.persistence.HibernateUtil;
import com.nutrehogar.sistemacontable.application.dto.JournalDTO;
import com.nutrehogar.sistemacontable.domain.core.CRUDRepositoryFactory;
import com.nutrehogar.sistemacontable.domain.model.JournalEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class JournalEntryRepoTest {

    JournalEntryRepository journalEntryRepository;
    @BeforeEach
    void setUp() {
        journalEntryRepository = CRUDRepositoryFactory.createRepository(JournalEntryRepository.class, JournalEntry.class, HibernateUtil.getSession());
    }

    @AfterEach
    void tearDown() {
        HibernateUtil.shutdown();
    }

    @Test
    void findAllByDateRange() {
        var list = journalEntryRepository.findAllByDateRange(LocalDate.of(2000,1,1), LocalDate.of(2040,1,1))
                .stream()
                .flatMap(journalEntry -> journalEntry.getLedgerRecords().stream()
                        .map(ledgerRecord -> new JournalDTO(
                                journalEntry.getId(),
                                journalEntry.getDate(),
                                ledgerRecord.getDocumentType(),
                                ledgerRecord.getAccount().getId(),
                                ledgerRecord.getVoucher(),
                                ledgerRecord.getReference(),
                                ledgerRecord.getDebit(),
                                ledgerRecord.getCredit()
                        ))
                )
                .toList(); // Java 17+, usa `collect(Collectors.toList())` en Java 8-16
        for( var a : list ) {
            System.out.println(a);
        }
    }
}