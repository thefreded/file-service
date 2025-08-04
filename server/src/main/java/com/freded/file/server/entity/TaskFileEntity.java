package com.freded.file.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
  @Id @GeneratedValue private UUID id;

  /** Original name of the uploaded file. */
  @NotNull(message = "File name cannot be null")
  private String fileName;

  /** Name /path to object on minion */
  @NotNull(message = "Object name cannot be null")
  private String objectName;

  /** MIME type or file extension of the file. */
  private String fileType;

  /** Timestamp when the file was created. Automatically set on creation. */
  private ZonedDateTime createdAt;

  /** Username or identifier of the user who uploaded the file. */
  private String uploadedBy;

  /** The taskId of task this file is associated with. */
  @NotNull private UUID taskId;

  /** Lifecycle callback to set creation timestamp before persisting dto. */
  @PrePersist
  protected void onCreate() {
    createdAt = ZonedDateTime.now(ZoneOffset.UTC);
  }
}
