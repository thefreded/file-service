package com.freded.file.server.controller;

import com.freded.dtos.TaskFileDTO;
import com.freded.file.client.entity.TaskFileUploadDTO;
import com.freded.file.server.CustomWebApplicationException;
import com.freded.file.server.entity.TaskEntity;
import com.freded.file.server.entity.TaskFileEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RequestScoped
public class TaskFileUploadService {
    private static final Logger LOG = Logger.getLogger(TaskFileUploadService.class);
    private static final String UPLOADS_FOLDER = "uploads";

    @Inject
    EntityManager entityManager;


    @Inject
    TaskFileMapper taskFileMapper;

    /**
     * Saves an uploaded file to the server filesystem and creates a database record.
     *
     * @param taskId     The task to associate the file with
     * @param uploadDTO  The data transfer object containing file information
     * @param uploadedBy User who uploaded the file
     * @return The created TaskFileEntity
     */
    @Transactional
    public TaskFileDTO saveFile(final String taskId, TaskFileUploadDTO uploadDTO, String uploadedBy) {

        try {
            TaskEntity task = entityManager.getReference(TaskEntity.class, taskId);


            if (task == null) {
                throw new CustomWebApplicationException("Task not found with ID: ", 400);
            }

            if (!task.getCreatedBy().equals(uploadedBy)) {
                throw new CustomWebApplicationException("Not allowed to upload task: ", 405);
            }


            // Sanitize the filename
            String sanitizedFileName = sanitizeFileName(uploadDTO.getFileName());

            // Create a unique file name to prevent collisions
            String uniqueFileName = UUID.randomUUID() + "_" + sanitizedFileName;

            // Create task-specific directory
            Path taskDir = createTaskDirectory(taskId);

            // Create the full file path
            Path filePath = taskDir.resolve(uniqueFileName);

            // Save the file to disk using byte array
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(uploadDTO.getFileData())) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            LOG.info("Saved file to: " + filePath);

            // Detect file type - use the one provided or detect it if not present
            String fileType = uploadDTO.getFileType();
            if (fileType == null || fileType.isEmpty()) {
                fileType = detectFileType(filePath);
            }

            // Create and persist the file entity
            TaskFileEntity fileEntity = new TaskFileEntity();
            fileEntity.setFileName(uniqueFileName);
            fileEntity.setFileType(fileType);
            fileEntity.setUploadedBy(uploadedBy);
            fileEntity.setTask(task);

            entityManager.persist(fileEntity);
            LOG.info("Created file entity with ID: " + fileEntity.getId());

            return taskFileMapper.toDTO(fileEntity);

        } catch (IOException ex) {
            LOG.error("Failed to save file: " + uploadDTO.getFileName(), ex);
            throw new CustomWebApplicationException("Failed to save file: " + ex.getMessage(), 500);
        }
    }

    /**
     * Creates the directory where task files will be stored.
     */
    private Path createTaskDirectory(String taskId) throws IOException {
        Path taskDir = Paths.get(getBasePath(), UPLOADS_FOLDER, sanitizePath(taskId));
        if (!Files.exists(taskDir)) {
            Files.createDirectories(taskDir);
        }
        return taskDir;
    }

    /**
     * Gets the base path for file storage.
     */
    private String getBasePath() {
        // You can configure this to use a property from config
        return System.getProperty("user.dir");
    }

    /**
     * Sanitizes a path segment to prevent directory traversal.
     */
    private String sanitizePath(String path) {
        return path.replaceAll("[^a-zA-Z0-9-]", "_");
    }

    /**
     * Sanitizes a filename to prevent security issues.
     */
    private String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "unknown_file";
        }

        // Extract just the filename part (no path)
        String name = Paths.get(fileName).getFileName().toString();

        // Remove potentially dangerous characters
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    /**
     * Detects file type using Java's built-in mechanism with fallback to extension-based detection.
     */
    private String detectFileType(Path filePath) throws IOException {
        // Try using Java's built-in content type detection
        String contentType = Files.probeContentType(filePath);

        // If that doesn't work, fall back to a simple extension check
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
