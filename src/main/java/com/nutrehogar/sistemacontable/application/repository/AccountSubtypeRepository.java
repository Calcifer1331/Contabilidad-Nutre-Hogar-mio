package com.nutrehogar.sistemacontable.application.repository;

import com.nutrehogar.sistemacontable.domain.AccountType;
import com.nutrehogar.sistemacontable.domain.model.AccountSubtype;
import com.nutrehogar.sistemacontable.exception.RepositoryException;

import java.util.List;

public interface AccountSubtypeRepository extends CRUDRepository<AccountSubtype, Integer> {
    /**
     * Busca todos los subtipos de cuenta por tipo de cuenta.
     *
     * @param accountType El tipo de cuenta.
     * @return Una lista de subtipos de cuenta que pertenecen al tipo de cuenta especificado.
     */
    List<AccountSubtype> findAllByAccountType(AccountType accountType) throws RepositoryException;
}
