package com.nutrehogar.sistemacontable.domain.repository;

import com.nutrehogar.sistemacontable.application.repository.LedgerRecordRepository;
import com.nutrehogar.sistemacontable.domain.core.CRUDRepositoryImpl;
import com.nutrehogar.sistemacontable.domain.model.LedgerRecord;

public class LedgerRecordRepo extends CRUDRepositoryImpl<LedgerRecord, Integer> implements LedgerRecordRepository {
    public LedgerRecordRepo() {
        super(LedgerRecord.class);
    }
}
