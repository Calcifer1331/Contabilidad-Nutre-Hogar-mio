package com.nutrehogar.sistemacontable.domain.model;

import com.nutrehogar.sistemacontable.domain.DocumentType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "journalEntry")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "ledger_record")
public class LedgerRecord extends AuditableEntity{
    public LedgerRecord(User user) {
        super(user);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "id_journal_document_number", referencedColumnName = "document_number"),
            @JoinColumn(name = "id_journal_document_type", referencedColumnName = "document_type")
    })
    JournalEntry journalEntry;

    @Column(name = "reference", columnDefinition = "TEXT")
    String reference;

    @ManyToOne
    @JoinColumn(name = "id_account", nullable = false)
    Account account;

    @Column(name = "debit", precision = 15, scale = 2)
    BigDecimal debit;

    @Column(name = "credit", precision = 15, scale = 2)
    BigDecimal credit;
}
