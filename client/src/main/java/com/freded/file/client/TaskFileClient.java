package com.freded.file;


import com.freded.dtos.TaskFileDTO;
import com.freded.file.boundary.TaskFile;
import com.freded.file.entity.TaskFileUploadDTO;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

public class TaskFileClient {

    @Inject
    @RestClient
    TaskFile restClient;

    public TaskFileDTO save(final String taskId, TaskFileUploadDTO form) {
        return restClient.uploadFileToTask(taskId, form);
    }

    public TaskFileDTO get(final String taskFileId) {
        return restClient.getFileDetails(taskFileId);
    }
}
