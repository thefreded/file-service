package com.freded.file.server.controller;

import com.freded.common.LoggedInUserInfo;
import com.freded.common.annotation.LoggedInUser;
import com.freded.dtos.TaskFileDTO;
import com.freded.dtos.TaskFilePaginationAndSortingDTO;
import com.freded.file.server.entity.TaskFileEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class TaskFileService {

  @Inject TaskFileRepository taskFileRepository;

  @Inject TaskFileMapper taskFileMapper;

  @Inject @LoggedInUser LoggedInUserInfo loggedInUserInfo;

  public TaskFileDTO getFile(final String taskFileId) {

    return taskFileMapper.toDTO(
        taskFileRepository.getByUploadedByAndId(loggedInUserInfo.getUsername(), this.stringToUuid(taskFileId)));
  }

  public List<TaskFileDTO> getAll(
      final String taskId, final TaskFilePaginationAndSortingDTO taskFilePaginationAndSortingDTO) {

    List<TaskFileEntity> taskFileEntities =
        taskFileRepository.readAll(loggedInUserInfo.getUsername(), taskId, taskFilePaginationAndSortingDTO);

    return taskFileMapper.toDTOList(taskFileEntities);
  }

  private UUID stringToUuid(String taskId) {
    if (taskId == null || taskId.trim().isEmpty()) {
      throw new IllegalArgumentException("Task ID cannot be null or empty");
    }

    try {
      return UUID.fromString(taskId.trim());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid UUID format: " + taskId, e);
    }
  }
}
