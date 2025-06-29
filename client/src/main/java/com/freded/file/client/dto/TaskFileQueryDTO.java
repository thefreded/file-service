package com.freded.file.client.dto;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** DTO for File queries from task services with pagination, sorting, and file loading options. */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskFileQueryDTO extends TaskFilePaginationAndSortingDTO {
  /** Whether to load associated files in the query result. Default is false. */
  @QueryParam("loadFiles")
  @DefaultValue("false")
  private boolean loadFiles;
}
