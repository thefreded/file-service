package com.freded.file.server.boundary;


import com.freded.dtos.TaskFileDTO;
import com.freded.entities.TaskEntity;
import com.freded.file.client.boundary.TaskFile;
import com.freded.file.server.CustomWebApplicationException;
import com.freded.file.server.controller.TaskFileService;
import com.freded.file.server.controller.TaskFileUploadService;
import com.freded.file.server.controller.UserService;
import com.freded.file.client.entity.TaskFileSortAndPaginationDTO;
import com.freded.file.client.entity.TaskFileUploadDTO;
import com.freded.task.client.TaskClient;
import jakarta.inject.Inject;

import java.util.List;


public class TaskFileImpl implements TaskFile {
    @Inject
    TaskFileService taskFileService;

    @Inject
    UserService userService;

    @Inject
    TaskFileUploadService fileUploadService;

    @Inject
    TaskClient taskClient;


    @Override
    public TaskFileDTO uploadFileToTask(final String taskId, final TaskFileUploadDTO form) {

        String uploadedBy = userService.getUsername();
        TaskEntity task = taskClient.get(taskId, true);


        if (task == null) {
            throw new CustomWebApplicationException("Task not found with ID: " + taskId, 400);
        }
        return fileUploadService.saveFile(task, form, uploadedBy);
    }

    @Override
    public TaskFileDTO getFileDetails(final String taskFileId) {
        String currentUser = userService.getUsername();
        return taskFileService.getFile(taskFileId, currentUser);
    }

    @Override
    public List<TaskFileDTO> getFilesForTask(final String taskId, final TaskFileSortAndPaginationDTO qParams) {
        String currentUser = userService.getUsername();
        return taskFileService.getAll(taskId, qParams, currentUser);
    }
}