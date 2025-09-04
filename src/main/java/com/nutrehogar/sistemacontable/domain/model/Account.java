package com.nutrehogar.sistemacontable.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


@Getter
@Setter
@ToString(exclude = "ledgerRecords")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class Account extends AuditableEntity {
    public Account(User user) {
        super(user);
    }

    public static final int MAX_ID_LENGTH = 5;
    public static final int MAX_CANONICAL_ID_LENGTH = 3;

    @Id
    @Column(name = "id", nullable = false)
    Integer id;

    @Column(name = "name", nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "id_account_subtype", nullable = false)
    AccountSubtype accountSubtype;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<LedgerRecord> ledgerRecords;

    //TODO hacer que al cambiar el id de los subtipoCuentas el id de estas cuentas tambien cambien;

    public void setId(Integer id) throws IllegalArgumentException, NullPointerException {
        Objects.requireNonNull(accountSubtype, "El subtipo de cuenta no puede ser nulo");

        int subtypeIdLength = accountSubtype.getId().toString().length();

        // Calcular el número máximo de dígitos permitidos para el ID de la cuenta
        int maxAccountIdDigits = MAX_ID_LENGTH - subtypeIdLength;

        // Validar que el ID de la cuenta no exceda el límite de dígitos
        if (id < 0 || id.toString().length() > maxAccountIdDigits) {
            throw new IllegalArgumentException("El ID de la cuenta debe tener hasta " + maxAccountIdDigits + " dígitos: " + id);
        }

        // Formatear el ID de la cuenta con el número de dígitos requeridos
        String accountIdFormatted = String.format("%0" + maxAccountIdDigits + "d", id);

        // Combinar el ID del AccountSubtype con el ID de la cuenta
        this.id = Integer.valueOf(accountSubtype.getId().toString() + accountIdFormatted);
    }

    public String getCanonicalId() {
        int subtypeIdLength = accountSubtype.getId().toString().length();
        return id.toString().substring(subtypeIdLength);
    }

    public String getFormattedId() {
        return getAccountSubtype().getFormattedId() + getCanonicalId();
    }

    public static @NotNull String getCellRenderer(Integer id) {
        if (id == null) return "";
        return getCellRenderer(id.toString());
    }

    public static @NotNull String getCellRenderer(String id) {
        if (id == null) return "";
        return id.charAt(0) + "." + id.substring(1);
    }
}
