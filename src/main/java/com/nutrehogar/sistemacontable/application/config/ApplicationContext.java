package com.nutrehogar.sistemacontable.application.config;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext implements AutoCloseable {
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public <T> void registerBean(Class<T> type, T instance) {
        beans.put(type, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        return (T) beans.get(type);
    }

    @Override
    public void close() {
        // Liberar recursos (por ejemplo, cerrar conexiones de base de datos)
        System.out.println("Cerrando ApplicationContext...");
    }
}