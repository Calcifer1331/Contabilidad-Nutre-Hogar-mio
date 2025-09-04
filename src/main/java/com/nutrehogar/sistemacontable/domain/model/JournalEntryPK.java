package com.nutrehogar.sistemacontable.domain.model;

import com.nutrehogar.sistemacontable.domain.DocumentType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString()
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode()
@Embeddable
public class JournalEntryPK implements Serializable {

    @Column(name = "document_number")
    Integer documentNumber;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "document_type")
    DocumentType documentType;
}
