package com.nutrehogar.sistemacontable.domain.model;

import com.nutrehogar.sistemacontable.application.dto.Pair;
import com.nutrehogar.sistemacontable.application.repository.Auditable;
import com.nutrehogar.sistemacontable.domain.AccountType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "accounts")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "account_subtype")
public class AccountSubtype extends AuditableEntity {
    public AccountSubtype(User user) {
        super(user);
    }

    public static final int MAX_ID_LENGTH = 4;
    public static final int MAX_CANONICAL_ID_LENGTH = 3;
    @Id
    @Column(name = "id", nullable = false)
    Integer id;

    @Column(name = "name", nullable = false)
    String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    AccountType accountType;

    @OneToMany(mappedBy = "accountSubtype", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Account> accounts;

    public void setId(@NotNull Integer id) throws IllegalArgumentException {
        Objects.requireNonNull(id, "El id del SubTipoCuenta no puede ser nulo");
        if (id < 0 || id > 999) {
            throw new IllegalArgumentException("El ID de la cuenta debe tener 3 dígitos (0-999): " + id);
        }
        this.id = Integer.valueOf(accountType.getId() + id.toString());
    }

    public Pair<Integer, Integer> getSeparateId() {
        var tipoCuentaId = accountType.getId();
        var subTipoCuentaId = id.toString().toCharArray()[0];
        return new Pair<>(id, accountType.getId());
    }

    public String getCanonicalId() {
        return id.toString().substring(1);
    }

    public String getFormattedId() {
        return getAccountType().getId() + "." + getCanonicalId();
    }
}
