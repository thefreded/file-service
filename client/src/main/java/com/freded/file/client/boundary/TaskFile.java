package com.freded.file.client.boundary;

import com.freded.dtos.TaskFileDTO;
import com.freded.dtos.TaskFilePaginationAndSortingDTO;
import com.freded.dtos.TaskFileUploadDTO;
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
  TaskFileDTO uploadFileToTask(
      @PathParam("taskId") final String taskId, @BeanParam TaskFileUploadDTO taskFileUploadDTO);

  @GET
  @Path("task/{taskId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  List<TaskFileDTO> getFilesForTask(
      @PathParam("taskId") final String taskId,
      @BeanParam final TaskFilePaginationAndSortingDTO taskFilePaginationAndSortingDTO);

  @GET
  @Path("{taskFileId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  TaskFileDTO getFileDetails(@PathParam("taskFileId") final String taskFileId);
}
