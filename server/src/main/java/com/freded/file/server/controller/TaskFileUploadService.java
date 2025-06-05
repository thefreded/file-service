package com.freded.file.server.controller;

import com.freded.dtos.TaskFileDTO;
import com.freded.dtos.TaskFileUploadDTO;
import com.freded.file.server.CustomWebApplicationException;
import com.freded.file.server.entity.TaskFileEntity;
import com.freded.file.server.infrastructure.cache.TaskCacheService;
import com.freded.file.server.infrastructure.events.TaskCreatedEvent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.jboss.logging.Logger;

@RequestScoped
public class TaskFileUploadService {
  private static final Logger LOG = Logger.getLogger(TaskFileUploadService.class);
  private static final String UPLOADS_FOLDER = "uploads";

  @Inject EntityManager entityManager;

  @Inject TaskFileMapper taskFileMapper;

  @Inject TaskCacheService taskCacheService;

  /**
   * Saves an uploaded file to the server filesystem and creates a database record.
   *
   * @param taskId The task to associate the file with
   * @param taskFileUploadDTO The data transfer object containing file information
   * @param uploadedBy User who uploaded the file
   * @return The created TaskFileEntity
   */
  @Transactional
  public TaskFileDTO saveFile(final String taskId, TaskFileUploadDTO taskFileUploadDTO, String uploadedBy) {

    try {
      TaskCreatedEvent taskEvent = taskCacheService.getTask(taskId).get();

      if (taskEvent == null) {
        throw new CustomWebApplicationException("Task not found with ID: " + taskId, 400);
      }

      if (!taskEvent.getCreatedBy().equals(uploadedBy)) {
        throw new CustomWebApplicationException("Not allowed to upload task: " + taskId, 403);
      }

      // Get the uploaded file
      var fileUpload = taskFileUploadDTO.getFileUpload();
      if (fileUpload == null) {
        throw new CustomWebApplicationException("No file uploaded", 400);
      }

      // Use the original filename from FileUpload if not provided in DTO
      String originalFileName = taskFileUploadDTO.getFileName();
      if (originalFileName == null || originalFileName.isEmpty()) {
        originalFileName = fileUpload.fileName();
      }

      // Sanitize the filename
      String sanitizedFileName = sanitizeFileName(originalFileName);

      // Create a unique file name to prevent collisions
      String uniqueFileName = UUID.randomUUID() + "_" + sanitizedFileName;

      // Create task-specific directory
      Path taskDir = createTaskDirectory(taskId);

      // Create the full file path
      Path filePath = taskDir.resolve(uniqueFileName);

      // Move the uploaded file to the target location
      Files.move(fileUpload.uploadedFile(), filePath, StandardCopyOption.REPLACE_EXISTING);

      LOG.info("Saved file to: " + filePath);

      // Try to get content type from FileUpload first
      String fileType = fileUpload.contentType();

      // detect from file
      if (fileType == null || fileType.isEmpty()) {
        fileType = detectFileType(filePath);
      }

      // Create and persist the file entity
      TaskFileEntity fileEntity = new TaskFileEntity();
      fileEntity.setFileName(uniqueFileName);
      fileEntity.setFileType(fileType);
      fileEntity.setUploadedBy(uploadedBy);
      fileEntity.setTaskId(taskId);

      entityManager.persist(fileEntity);
      LOG.info("Created file entity with ID: " + fileEntity.getId());

      return taskFileMapper.toDTO(fileEntity);

    } catch (CustomWebApplicationException e) {
      // Re-throw custom exceptions as-is
      throw e;
    } catch (ExecutionException e) {
      LOG.error("Failed to retrieve task from cache: " + taskId, e);
      Throwable cause = e.getCause();
      if (cause instanceof RuntimeException) {
        throw (RuntimeException) cause;
      }
      throw new CustomWebApplicationException("Failed to retrieve task: " + cause.getMessage(), 500);
    } catch (Exception e) {
      LOG.error("Unexpected error while saving file for task: " + taskId, e);
      throw new CustomWebApplicationException("Unexpected error occurred: " + e.getMessage(), 500);
    }
  }

  /** Creates the directory where task files will be stored. */
  private Path createTaskDirectory(String taskId) throws IOException {
    Path taskDir = Paths.get(getBasePath(), UPLOADS_FOLDER, sanitizePath(taskId));
    if (!Files.exists(taskDir)) {
      Files.createDirectories(taskDir);
    }
    return taskDir;
  }

  /** Gets the base path for file storage. */
  private String getBasePath() {
    return System.getProperty("user.dir");
  }

  /** Sanitizes a path segment to prevent directory traversal. */
  private String sanitizePath(String path) {
    return path.replaceAll("[^a-zA-Z0-9-]", "_");
  }

  /** Sanitizes a filename to prevent security issues. */
  private String sanitizeFileName(String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      return "unknown_file";
    }
    String name = Paths.get(fileName).getFileName().toString();

    // Remove potentially dangerous characters
    return name.replaceAll("[^a-zA-Z0-9._-]", "_");
  }

  /** Detects file type using Java's built-in mechanism with fallback to extension-based detection. */
  private String detectFileType(Path filePath) throws IOException {

    String contentType = Files.probeContentType(filePath);

    if (contentType == null || contentType.isEmpty()) {
      String fileName = filePath.getFileName().toString().toLowerCase();
      int dotIndex = fileName.lastIndexOf('.');

      if (dotIndex > 0) {
        switch (fileName.substring(dotIndex)) {
          case ".pdf":
            return "application/pdf";
          case ".jpg":
          case ".jpeg":
            return "image/jpeg";
          case ".png":
            return "image/png";
          case ".txt":
            return "text/plain";
          case ".doc":
          case ".docx":
            return "application/msword";
          case ".xls":
          case ".xlsx":
            return "application/vnd.ms-excel";
          case ".csv":
            return "text/csv";
            // Add other common types as needed
        }
      }

      // Default if we can't determine it
      return "application/octet-stream";
    }

    return contentType;
  }
}
