package com.freded.file.client.dto;

import com.freded.common.dto.PaginationAndSortingDTO;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** DTO for TaskFile pagination and sorting with specific default sorting field. */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskFilePaginationAndSortingDTO extends PaginationAndSortingDTO {

  /** Field that should be used as sorting value. Default is {@code fileName}. */
  @QueryParam("sortBy")
  @DefaultValue("fileName")
  private String sortBy;
}
