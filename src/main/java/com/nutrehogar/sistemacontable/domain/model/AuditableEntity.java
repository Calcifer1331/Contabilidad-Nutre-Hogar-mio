package com.nutrehogar.sistemacontable.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Optional;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AuditableEntity {

    @Transient
    private User user;

    @Column(name = "created_by", updatable = false, nullable = false)
    String createdBy;

    @Column(name = "updated_by")
    String updatedBy;

    @Column(name = "deleted_by")
    String deletedBy;

    @Column(name = "created_at", updatable = false, nullable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

    public AuditableEntity(User user) {
        this.user = Optional.ofNullable(user)
                .orElseThrow(() -> new IllegalArgumentException("user is null"));
    }

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.createdBy = getUsername();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = getUsername();
    }

    private String getUsername() {
        return Optional.ofNullable(user.getUsername())
                .orElse("unknown user");
    }

}
