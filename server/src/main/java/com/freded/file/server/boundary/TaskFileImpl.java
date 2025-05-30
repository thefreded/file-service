package com.freded.file.server.boundary;


import com.freded.dtos.TaskFileDTO;
import com.freded.file.client.boundary.TaskFile;
import com.freded.file.client.entity.TaskFileSortAndPaginationDTO;
import com.freded.file.client.entity.TaskFileUploadDTO;
import com.freded.file.server.controller.TaskFileService;
import com.freded.file.server.controller.TaskFileUploadService;
import com.freded.file.server.controller.UserService;
import jakarta.inject.Inject;

import java.util.List;


public class TaskFileImpl implements TaskFile {
    @Inject
    TaskFileService taskFileService;

    @Inject
    UserService userService;

    @Inject
    TaskFileUploadService fileUploadService;


    @Override
    public TaskFileDTO uploadFileToTask(final String taskId, final TaskFileUploadDTO form) {

        String uploadedBy = userService.getUsername();
        return fileUploadService.saveFile(taskId, form, uploadedBy);
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