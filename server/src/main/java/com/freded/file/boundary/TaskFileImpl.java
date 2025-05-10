package com.freded.file.boundary;


import com.freded.dtos.TaskFileDTO;
import com.freded.entities.TaskEntity;
import com.freded.file.CustomWebApplicationException;
import com.freded.file.controller.TaskFileService;
import com.freded.file.controller.TaskFileUploadService;
import com.freded.file.controller.UserService;
import com.freded.file.entity.TaskFileSortAndPaginationDTO;
import com.freded.file.entity.TaskFileUploadDTO;
import com.freded.task.TaskClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("document")
public class TaskFileImpl implements TaskFile {
    @Inject
    TaskFileService taskFileService;

    @Inject
    UserService userService;

    @Inject
    TaskFileUploadService fileUploadService;

    @Inject
    TaskClient taskClient;


    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
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