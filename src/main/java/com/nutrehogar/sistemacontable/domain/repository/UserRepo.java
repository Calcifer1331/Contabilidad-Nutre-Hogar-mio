package com.nutrehogar.sistemacontable.domain.repository;

import com.nutrehogar.sistemacontable.application.repository.UserRepository;
import com.nutrehogar.sistemacontable.domain.core.CRUDRepositoryImpl;
import com.nutrehogar.sistemacontable.domain.model.User;

public class UserRepo extends CRUDRepositoryImpl<User, Integer> implements UserRepository {

    public UserRepo() {
        super(User.class);
    }
}
