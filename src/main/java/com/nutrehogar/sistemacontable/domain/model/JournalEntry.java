package com.nutrehogar.sistemacontable.domain.model;

import com.nutrehogar.sistemacontable.domain.DocumentType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "ledgerRecords")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "journal_entry")
public class JournalEntry extends AuditableEntity {
    public JournalEntry(User user) {
        super(user);
    }

    @EmbeddedId
    JournalEntryPK id;

    @Column(name = "name", columnDefinition = "TEXT", nullable = false)
    String name;

    @Column(name = "concept", columnDefinition = "TEXT")
    String concept;

    @Column(name = "check_number", columnDefinition = "TEXT")
    String checkNumber;

    @Column(name = "date", nullable = false)
    LocalDate date;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<LedgerRecord> ledgerRecords = new ArrayList<>();

}
