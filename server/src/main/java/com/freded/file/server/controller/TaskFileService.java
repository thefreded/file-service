package com.freded.file.server.controller;

import com.freded.common.LoggedInUserInfo;
import com.freded.common.annotation.LoggedInUser;
import com.freded.file.client.dto.TaskFileDTO;
import com.freded.file.client.dto.TaskFilePaginationAndSortingDTO;
import com.freded.file.server.CustomWebApplicationException;
import com.freded.file.server.entity.TaskFileEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.jboss.logging.Logger;

@ApplicationScoped
@Transactional
public class TaskFileService {

  private static final Logger LOG = Logger.getLogger(TaskFileService.class);

  @Inject TaskFileRepository taskFileRepository;

  @Inject TaskFileMapper taskFileMapper;

  @Inject @LoggedInUser LoggedInUserInfo loggedInUserInfo;

  @Inject MinioUploadService minioUploadService;

  public TaskFileDTO getFile(final UUID taskFileId) {

    return taskFileMapper.toDTO(taskFileRepository.getByUploadedByAndId(loggedInUserInfo.getUsername(), taskFileId));
  }

  public String getFileUrl(final UUID taskFileId) {
    final TaskFileEntity taskFileEntity =
        taskFileRepository.getByUploadedByAndId(loggedInUserInfo.getUsername(), taskFileId);

    if (taskFileEntity == null) {
      throw new CustomWebApplicationException("File not found for user", 404);
    }

    try {
      return minioUploadService.getTempUrl(taskFileEntity.getObjectName());
    } catch (Exception ex) {
      LOG.error("Failed to generate temporary URL for file: " + taskFileId, ex);
      throw new CustomWebApplicationException("Failed to generate file URL: " + ex.getMessage(), 500);
    }
  }

  public List<TaskFileDTO> getAll(
      final UUID taskId, final TaskFilePaginationAndSortingDTO taskFilePaginationAndSortingDTO) {

    final List<TaskFileEntity> taskFileEntities =
        taskFileRepository.readAll(loggedInUserInfo.getUsername(), taskId, taskFilePaginationAndSortingDTO);

    return taskFileMapper.toDTOList(taskFileEntities);
  }
}
