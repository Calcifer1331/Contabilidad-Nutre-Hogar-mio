package com.nutrehogar.sistemacontable.application.repository;

public interface Auditable {
    String getCreatedAt();

    String getCreatedBy();

    String getUpdatedAt();

    String getUpdatedBy();
}
