package com.nutrehogar.sistemacontable.domain;

import lombok.Getter;

@Getter
public enum DocumentType {
    INCOME("INGRESO"), EXPENDITURE("EGRESO"), ADJUSTMENT("AJUSTE");
    private final String name;

    DocumentType(String name) {
        this.name = name;
    }
}
