package com.freded.file.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a file associated with a task. Contains file metadata and maintains relationship with TaskEntity.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskFileEntity {

  /** Unique identifier for the file. Auto-generated UUID string. */
  @Id private String id = UUID.randomUUID().toString();

  /** Original name of the uploaded file. Must be unique and cannot be null. */
  @NotNull(message = "File name cannot be null")
  @Column(unique = true, nullable = false)
  private String fileName;

  /** MIME type or file extension of the file. */
  private String fileType;

  /** Timestamp when the file was created. Automatically set on dto creation. */
  private LocalDateTime createdAt;

  /** Username or identifier of the user who uploaded the file. */
  private String uploadedBy;

  /** The taskId of task this file is associated with. */
  @NotNull private String taskId;

  /** Lifecycle callback to set creation timestamp before persisting dto. */
  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }
}
