package com.freded.file.client;

import com.freded.dtos.TaskFileDTO;
import com.freded.dtos.TaskFilePaginationAndSortingDTO;
import com.freded.dtos.TaskFileUploadDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class TaskFileClient {

  @Inject @RestClient TaskFileRestClient taskFileRestClient;

  public TaskFileDTO uploadFileToTask(final String taskId, final TaskFileUploadDTO taskFileUploadDTO) {
    return taskFileRestClient.uploadFileToTask(taskId, taskFileUploadDTO);
  }

  public TaskFileDTO getFileDetails(final String taskFileId) {
    return taskFileRestClient.getFileDetails(taskFileId);
  }

  public List<TaskFileDTO> getFilesForTask(
      final String taskId, final TaskFilePaginationAndSortingDTO taskFilePaginationAndSortingDTO) {
    return taskFileRestClient.getFilesForTask(taskId, taskFilePaginationAndSortingDTO);
  }
}
