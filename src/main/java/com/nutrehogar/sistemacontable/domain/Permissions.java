package com.nutrehogar.sistemacontable.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public enum Permissions {
    VIEW("Ver"),
    CREATE("Crea");
    String name;
}
