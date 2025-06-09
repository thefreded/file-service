package com.freded.file.server.controller;

import com.freded.annotations.LoggedInUser;
import com.freded.dtos.TaskFileDTO;
import com.freded.dtos.TaskFilePaginationAndSortingDTO;
import com.freded.file.server.entity.TaskFileEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional
public class TaskFileService {

  @Inject TaskFileRepository taskFileRepository;

  @Inject TaskFileMapper taskFileMapper;

  @Inject @LoggedInUser String currentUser;

  public TaskFileDTO getFile(final String taskFileId) {

    return taskFileMapper.toDTO(taskFileRepository.getByUploadedByAndId(currentUser, taskFileId));
  }

  public List<TaskFileDTO> getAll(
      final String taskId, final TaskFilePaginationAndSortingDTO taskFilePaginationAndSortingDTO) {

    List<TaskFileEntity> taskFileEntities =
        taskFileRepository.readAll(currentUser, taskId, taskFilePaginationAndSortingDTO);

    return taskFileMapper.toDTOList(taskFileEntities);
  }
}
