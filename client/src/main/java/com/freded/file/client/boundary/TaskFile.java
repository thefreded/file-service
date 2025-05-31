package com.freded.file.client.boundary;

import com.freded.dtos.TaskFileDTO;
import com.freded.dtos.TaskFileSortAndPaginationDTO;
import com.freded.file.client.dto.TaskFileUploadDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@RolesAllowed({"user", "admin"})
@Path("document")
public interface TaskFile {

  @POST
  @Path("task/{taskId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  TaskFileDTO uploadFileToTask(@PathParam("taskId") final String taskId, TaskFileUploadDTO form);

  @GET
  @Path("task/{taskId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  List<TaskFileDTO> getFilesForTask(
      @PathParam("taskId") final String taskId, @BeanParam final TaskFileSortAndPaginationDTO qParams);

  @GET
  @Path("{taskFileId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  TaskFileDTO getFileDetails(@PathParam("taskFileId") final String taskFileId);
}
