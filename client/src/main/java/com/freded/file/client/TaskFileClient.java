package com.freded.file.client;


import com.freded.dtos.TaskFileDTO;
import com.freded.file.client.entity.TaskFileSortAndPaginationDTO;
import com.freded.file.client.entity.TaskFileUploadDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class TaskFileClient {

    @Inject
    @RestClient
    TaskFileRestClient taskFileRestClient;

    public TaskFileDTO uploadFileToTask(final String taskId, final TaskFileUploadDTO form) {
        return taskFileRestClient.uploadFileToTask(taskId, form);
    }

    public TaskFileDTO getFileDetails(final String taskFileId) {
        return taskFileRestClient.getFileDetails(taskFileId);
    }

    public List<TaskFileDTO> getFilesForTask(final String taskId, final TaskFileSortAndPaginationDTO qParams) {
        return taskFileRestClient.getFilesForTask(taskId, qParams);
    }
}
