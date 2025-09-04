package com.nutrehogar.sistemacontable.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Enum que define los tipos que pueden ser las cunetas.
 * <p>
 * Dependiendo del tipo de cuenta el saldo es el que suma es el haber o debe y vise versa con la resta
 * <p>
 * {@code COSTO} es el unico que su valor está mal
 *
 * @author Calcifer1331
 * @see com.nutrehogar.sistemacontable.domain.model.TipoCuenta
 * @see com.nutrehogar.sistemacontable.ui.view.components.MayorGenTableModel
 */
@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum AccountType {
    ASSETS(1, "ACTIVO") {
        @Override
        public BigDecimal getBalance(BigDecimal balance, BigDecimal credit, BigDecimal debit) {
            return balance.add(debit, MATH_CONTEXT).subtract(credit, MATH_CONTEXT).setScale(2, RoundingMode.HALF_UP);
        }
    },
    LIABILITIES(2, "PASIVO") {
        @Override
        public BigDecimal getBalance(BigDecimal balance, BigDecimal credit, BigDecimal debit) {
            return balance.add(credit, MATH_CONTEXT).subtract(debit, MATH_CONTEXT).setScale(2, RoundingMode.HALF_UP);
        }
    },
    EQUITY(3, "PATRIMONIO") {
        @Override
        public BigDecimal getBalance(BigDecimal balance, BigDecimal credit, BigDecimal debit) {
            return balance.add(credit, MATH_CONTEXT).subtract(debit, MATH_CONTEXT).setScale(2, RoundingMode.HALF_UP);
        }
    },
    INCOME(4, "INGRESO") {
        @Override
        public BigDecimal getBalance(BigDecimal balance, BigDecimal credit, BigDecimal debit) {
            return balance.add(credit, MATH_CONTEXT).subtract(debit, MATH_CONTEXT).setScale(2, RoundingMode.HALF_UP);
        }
    },
    EXPENSE(5, "GASTO") {
        @Override
        public BigDecimal getBalance(BigDecimal balance, BigDecimal credit, BigDecimal debit) {
            return balance.add(debit, MATH_CONTEXT).subtract(credit, MATH_CONTEXT).setScale(2, RoundingMode.HALF_UP);
        }
    },
    //TODO: El metodo no esta bien implementado
    COST(6, "COSTO") {
        @Override
        public BigDecimal getBalance(BigDecimal balance, BigDecimal credit, BigDecimal debit) {
            return balance.add(credit, MATH_CONTEXT).subtract(debit, MATH_CONTEXT).setScale(2, RoundingMode.HALF_UP);
        }
    };
    /**
     * El contexto de las operaciones es el maximo
     */
    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;
    /**
     * Es el id con el que esta registrado en la base de datos
     */
    final int id;
    final String name;

    public static @NotNull AccountType fromId(int id) {
        for (AccountType tipo : values()) {
            if (tipo.getId() == id) {
                return tipo;
            }
        }
        return ASSETS;
    }

    /**
     * Dependiendo del tipo de cuenta el saldo es el que suma es el haber o debe y vise versa con la resta
     *
     * @param balance
     * @param credit
     * @param debit
     * @return suma de {@code saldo} y el resultado de la resta o suma de {@code haber} y {@code debe}
     */
    public abstract BigDecimal getBalance(BigDecimal balance, BigDecimal credit, BigDecimal debit);

    public static @NotNull String getCellRenderer(@NotNull AccountType tipo) {
        return tipo.getId() + " " + tipo.getName();
    }
}
