package com.freded.file.server.controller;

import com.freded.common.LoggedInUserInfo;
import com.freded.common.annotation.LoggedInUser;
import com.freded.file.client.dto.TaskFileDTO;
import com.freded.file.client.dto.TaskFileUploadDTO;
import com.freded.file.server.CustomWebApplicationException;
import com.freded.file.server.entity.TaskFileEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@ApplicationScoped
@Transactional
public class TaskFileUploadService {

  private static final Logger LOG = Logger.getLogger(TaskFileUploadService.class);

  @Inject EntityManager entityManager;
  @Inject TaskFileMapper taskFileMapper;
  @Inject @LoggedInUser LoggedInUserInfo loggedInUserInfo;
  @Inject MinioUploadService minioUploadService;

  public TaskFileDTO saveFile(final String taskId, final TaskFileUploadDTO taskFileUploadDTO) {
    try {
      validateInput(taskId, taskFileUploadDTO);

      var fileUpload = taskFileUploadDTO.getFileUpload();
      String loggedInUsername = loggedInUserInfo.getUsername();

      String sanitizedFileName = extractAndSanitizeFileName(fileUpload, taskFileUploadDTO);
      String fileType = fileUpload.contentType();
      String objectName = buildObjectName(loggedInUsername, taskId, sanitizedFileName);

      uploadToMinio(fileUpload, objectName, fileType);
      TaskFileEntity fileEntity = createFileEntity(sanitizedFileName, fileType, objectName, loggedInUsername, taskId);

      return saveAndReturnDTO(fileEntity);

    } catch (Exception ex) {
      handleError(taskFileUploadDTO.getFileName(), ex);
      throw new CustomWebApplicationException("Failed to save file: " + ex.getMessage(), 500);
    }
  }

  private void validateInput(String taskId, TaskFileUploadDTO taskFileUploadDTO) {
    if (taskId == null || taskId.isEmpty()) {
      throw new CustomWebApplicationException("Task not found with ID", 400);
    }

    if (taskFileUploadDTO.getFileUpload() == null) {
      throw new CustomWebApplicationException("No file uploaded", 400);
    }
  }

  private String extractAndSanitizeFileName(FileUpload fileUpload, TaskFileUploadDTO taskFileUploadDTO) {
    String originalFileName = fileUpload.fileName();
    if (originalFileName == null || originalFileName.isEmpty()) {
      originalFileName = taskFileUploadDTO.getFileName();
    }
    return sanitizeFileName(originalFileName);
  }

  private String buildObjectName(String username, String taskId, String fileName) {
    return String.format("user/%s/%s/%s", username, taskId, fileName);
  }

  private void uploadToMinio(FileUpload fileUpload, String objectName, String fileType) throws Exception {
    Path filePath = fileUpload.uploadedFile();
    try (InputStream inputStream = Files.newInputStream(filePath)) {
      minioUploadService.uploadFile(objectName, inputStream, fileUpload.size(), fileType);
    }
  }

  private TaskFileEntity createFileEntity(
      String fileName, String fileType, String objectName, String uploadedBy, String taskId) {
    TaskFileEntity fileEntity = new TaskFileEntity();
    fileEntity.setFileName(fileName);
    fileEntity.setFileType(fileType);
    fileEntity.setObjectName(objectName);
    fileEntity.setUploadedBy(uploadedBy);
    fileEntity.setTaskId(taskId);
    return fileEntity;
  }

  private TaskFileDTO saveAndReturnDTO(TaskFileEntity fileEntity) {
    entityManager.persist(fileEntity);
    LOG.info("Created file dto with ID: " + fileEntity.getId());
    return taskFileMapper.toDTO(fileEntity);
  }

  private void handleError(String fileName, Exception ex) {
    LOG.error("Failed to save file: " + fileName, ex);
  }

  private String sanitizeFileName(String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      return "unknown_file";
    }
    return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
  }
}
