package com.nutrehogar.sistemacontable.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.batik.dom.AbstractEntity;

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

    @Id
    @Column(name = "id")
    Integer id;

    @Column(name = "check_number", columnDefinition = "TEXT")
    String checkNumber;

    @Column(name = "date", nullable = false)
    LocalDate date;

    @Column(name = "name", columnDefinition = "TEXT", nullable = false)
    String name;

    @Column(name = "concept", columnDefinition = "TEXT")
    String concept;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<LedgerRecord> ledgerRecords = new ArrayList<>();

}
