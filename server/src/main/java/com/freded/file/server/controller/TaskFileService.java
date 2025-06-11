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

@ApplicationScoped
@Transactional
public class TaskFileService {

  @Inject TaskFileRepository taskFileRepository;

  @Inject TaskFileMapper taskFileMapper;

  @Inject @LoggedInUser LoggedInUserInfo loggedInUserInfo;

  public TaskFileDTO getFile(final String taskFileId) {

    return taskFileMapper.toDTO(taskFileRepository.getByUploadedByAndId(loggedInUserInfo.getUsername(), taskFileId));
  }

  public List<TaskFileDTO> getAll(
      final String taskId, final TaskFilePaginationAndSortingDTO taskFilePaginationAndSortingDTO) {

    List<TaskFileEntity> taskFileEntities =
        taskFileRepository.readAll(loggedInUserInfo.getUsername(), taskId, taskFilePaginationAndSortingDTO);

    return taskFileMapper.toDTOList(taskFileEntities);
  }
}
