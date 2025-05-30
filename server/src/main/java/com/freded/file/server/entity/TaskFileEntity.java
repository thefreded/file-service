package com.freded.file.server.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a file associated with a task.
 * Contains file metadata and maintains relationship with TaskEntity.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskFileEntity {

    /**
     * Unique identifier for the file.
     * Auto-generated UUID string.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Original name of the uploaded file.
     * Must be unique and cannot be null.
     */
    @NotNull(message = "File name cannot be null")
    @Column(unique = true, nullable = false)
    private String fileName;

    /**
     * MIME type or file extension of the file.
     */
    private String fileType;

    /**
     * Timestamp when the file was created.
     * Automatically set on entity creation.
     */
    private LocalDateTime createdAt;

    /**
     * Username or identifier of the user who uploaded the file.
     */
    private String uploadedBy;

    /**
     * The task this file is associated with.
     * Many files can belong to one task.
     */
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @JsonIgnore
    private TaskEntity task;

    /**
     * Lifecycle callback to set creation timestamp before persisting entity.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
