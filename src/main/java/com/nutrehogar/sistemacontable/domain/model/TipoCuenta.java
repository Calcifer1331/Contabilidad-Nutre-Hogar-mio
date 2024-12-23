package com.nutrehogar.sistemacontable.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "subTipoCuenta")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tipo_cuenta")
public class TipoCuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "nombre")
    String nombre;

    @OneToMany(mappedBy = "tipoCuenta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<SubTipoCuenta> subTipoCuenta;
}
