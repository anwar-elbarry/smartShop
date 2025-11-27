package org.example.smartshop.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class IsDeleted {

    @Column(nullable = false)
    private boolean deleted = false;

    // Soft delete method
    public void delete() {
        this.deleted = true;
    }

    // Check if entity is deleted
    public boolean checkIsDeleted() {
        return deleted;
    }
}
