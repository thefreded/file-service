package com.freded.file.server.boundary;

import com.freded.file.client.boundary.TaskFileResource;
import com.freded.file.client.dto.TaskFileDTO;
import com.freded.file.client.dto.TaskFilePaginationAndSortingDTO;
import com.freded.file.client.dto.TaskFileUploadDTO;
import com.freded.file.server.controller.TaskFileService;
import com.freded.file.server.controller.TaskFileUploadService;
import jakarta.inject.Inject;
import java.util.List;

public class TaskFileResourceImpl implements TaskFileResource {
  @Inject TaskFileService taskFileService;

  @Inject TaskFileUploadService fileUploadService;

  @Override
  public TaskFileDTO uploadFileToTask(final String taskId, final TaskFileUploadDTO taskFileUploadDTO) {

    return fileUploadService.saveFile(taskId, taskFileUploadDTO);
  }

  @Override
  public TaskFileDTO getFileDetails(final String taskFileId) {
    return taskFileService.getFile(taskFileId);
  }

  @Override
  public String getFileUrl(String taskFileId) {
    return taskFileService.getFileUrl(taskFileId);
  }

  @Override
  public List<TaskFileDTO> getFilesForTask(
      final String taskId, final TaskFilePaginationAndSortingDTO taskFilePaginationAndSortingDTO) {
    return taskFileService.getAll(taskId, taskFilePaginationAndSortingDTO);
  }
}
