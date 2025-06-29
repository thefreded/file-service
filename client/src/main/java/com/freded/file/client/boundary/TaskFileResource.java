package com.freded.file.client.boundary;

import com.freded.file.client.dto.TaskFileDTO;
import com.freded.file.client.dto.TaskFilePaginationAndSortingDTO;
import com.freded.file.client.dto.TaskFileUploadDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@RolesAllowed({"user", "admin"})
@Path("document")
public interface TaskFileResource {

  @POST
  @Path("task/{taskId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  TaskFileDTO uploadFileToTask(
      @PathParam("taskId") final String taskId, @BeanParam TaskFileUploadDTO taskFileUploadDTO);

  @GET
  @Path("task/{taskId}")
  @Produces(MediaType.APPLICATION_JSON)
  List<TaskFileDTO> getFilesForTask(
      @PathParam("taskId") final String taskId,
      @BeanParam final TaskFilePaginationAndSortingDTO taskFilePaginationAndSortingDTO);

  @GET
  @Path("{taskFileId}")
  @Produces(MediaType.APPLICATION_JSON)
  TaskFileDTO getFileDetails(@PathParam("taskFileId") final String taskFileId);

  @GET
  @Path("preview/{taskFileId}")
  @Produces(MediaType.TEXT_PLAIN)
  String getFileUrl(@PathParam("taskFileId") final String taskFileId);
}
