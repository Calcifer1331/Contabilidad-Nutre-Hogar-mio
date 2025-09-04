package com.nutrehogar.sistemacontable.application.config;

import com.nutrehogar.sistemacontable.domain.core.WriteExecutor;
import com.nutrehogar.sistemacontable.infrastructure.persistence.HibernateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ApplicationContext {
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public <T> void registerBean(Class<T> type, T instance) {
        beans.put(type, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        return (T) beans.get(type);
    }
}