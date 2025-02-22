package com.nutrehogar.sistemacontable.domain.model;

import com.nutrehogar.sistemacontable.domain.Permissions;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user")
public class User extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    String password;

    String username;

    @Column(name = "is_enable")
    boolean isEnable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Permissions permissions;

    public boolean isAuthorized() {
        return permissions.equals(Permissions.CREATE);
    }

    public static boolean isAuthorized(User user) {
        return user != null && user.isAuthorized();
    }
}