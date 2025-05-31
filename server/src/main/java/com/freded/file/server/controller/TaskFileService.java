package com.freded.file.server.controller;

import com.freded.dtos.TaskFileDTO;
import com.freded.dtos.TaskFileSortAndPaginationDTO;
import com.freded.file.server.entity.TaskFileEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.util.List;

@RequestScoped
public class TaskFileService {

  @Inject TaskFileRepository taskFileRepository;

  @Inject TaskFileMapper taskFileMapper;

  public TaskFileDTO getFile(final String taskFileId, final String currentUser) {
    return taskFileMapper.toDTO(taskFileRepository.getByUploadedByAndId(currentUser, taskFileId));
  }

  public List<TaskFileDTO> getAll(
      final String taskId, final TaskFileSortAndPaginationDTO qParams, final String currentUser) {

    List<TaskFileEntity> taskFileEntities = taskFileRepository.readAll(currentUser, taskId, qParams);

    return taskFileMapper.toDTOList(taskFileEntities);
  }
}
