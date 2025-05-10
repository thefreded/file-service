package com.freded.file.client.entity;


import com.freded.dtos.PaginationAndSortingDTO;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskFileSortAndPaginationDTO extends PaginationAndSortingDTO {

    /**
     * Field that should be used as sorting value.
     * Default is {@code uploadedBy}.
     */
    @QueryParam("sortBy")
    @DefaultValue("fileName")
    private String sortBy;
}
