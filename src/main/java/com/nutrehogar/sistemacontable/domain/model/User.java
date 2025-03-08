package com.nutrehogar.sistemacontable.domain.model;

import com.nutrehogar.sistemacontable.domain.Permissions;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user")
public class User extends AuditableEntity {
    public static final int MIN_LENGTH = 4;
    public static final int MAX_LENGTH = 120;

    public User(User user) {
        super(user);
    }

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