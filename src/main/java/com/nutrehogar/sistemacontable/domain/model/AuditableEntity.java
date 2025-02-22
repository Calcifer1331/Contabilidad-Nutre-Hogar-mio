package com.nutrehogar.sistemacontable.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AuditableEntity {

    @Transient
    public User user;

    @Column(name = "created_by", updatable = false)
    String createdBy;

    @Column(name = "updated_by")
    String updatedBy;

    @Column(name = "deleted_by")
    String deletedBy;

    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

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
        return user.getUsername() == null ? "no hay usuario!" : user.getUsername();
    }
}