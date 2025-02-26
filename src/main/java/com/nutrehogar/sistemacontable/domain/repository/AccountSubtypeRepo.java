package com.nutrehogar.sistemacontable.domain.repository;

import com.nutrehogar.sistemacontable.application.repository.AccountSubtypeRepository;
import com.nutrehogar.sistemacontable.domain.AccountType;
import com.nutrehogar.sistemacontable.domain.core.CRUDRepositoryImpl;
import com.nutrehogar.sistemacontable.domain.core.TransactionManager;
import com.nutrehogar.sistemacontable.domain.model.AccountSubtype;
import com.nutrehogar.sistemacontable.exception.RepositoryException;

import java.util.List;

public class AccountSubtypeRepo extends CRUDRepositoryImpl<AccountSubtype, Integer>
        implements AccountSubtypeRepository {

    public AccountSubtypeRepo() {
        super(AccountSubtype.class);
    }

    @Override
    public List<AccountSubtype> findAllByAccountType(AccountType accountType) throws RepositoryException {
        return TransactionManager.executeInTransaction(session -> session.createQuery(
                        "FROM AccountSubtype WHERE accountType = :accountType ORDER BY id",
                        AccountSubtype.class
                )
                .setParameter("accountType", accountType)
                .list());
    }
}