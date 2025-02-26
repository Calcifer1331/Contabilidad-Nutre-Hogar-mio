package com.nutrehogar.sistemacontable.domain.repository;

import com.nutrehogar.sistemacontable.application.repository.AccountRepository;
import com.nutrehogar.sistemacontable.domain.core.CRUDRepositoryImpl;
import com.nutrehogar.sistemacontable.domain.model.Account;

public class AccountRepo extends CRUDRepositoryImpl<Account, Integer> implements AccountRepository {

    public AccountRepo() {
        super(Account.class);
    }
}
