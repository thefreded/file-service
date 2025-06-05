package com.freded.file.server.controller;

import com.freded.dtos.PaginationAndSortingDTO;
import com.freded.dtos.TaskFilePaginationAndSortingDTO;
import com.freded.file.server.entity.TaskFileEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

@RequestScoped
public class PaginationAndSortingService {
  private static final String DESC = "DESC";

  /**
   * Applies sorting to a list of task files.
   *
   * @param cb CriteriaBuilder for constructing sort expressions
   * @param cbQuery CriteriaQuery for task files to apply sorting to
   * @param root Root task file entity being queried
   * @param taskFilePaginationAndSortingDTO Sorting parameters with sortBy field and sortOrder direction for task files
   */
  public void sort(
      final CriteriaBuilder cb,
      final CriteriaQuery<TaskFileEntity> cbQuery,
      final Root<TaskFileEntity> root,
      final TaskFilePaginationAndSortingDTO taskFilePaginationAndSortingDTO) {

    Order order =
        DESC.equalsIgnoreCase(taskFilePaginationAndSortingDTO.getSortOrder())
            ? cb.desc(root.get(taskFilePaginationAndSortingDTO.getSortBy()))
            : cb.asc(root.get(taskFilePaginationAndSortingDTO.getSortBy()));

    cbQuery.orderBy(order);
  }

  /**
   * Applies pagination to a list of task files.
   *
   * @param typedQuery TypedQuery for task files to apply pagination to
   * @param taskFilePaginationAndSortingDTO Pagination parameters with offset and limit for task files
   */
  public <T, Q extends PaginationAndSortingDTO> void paginate(
      final TypedQuery<TaskFileEntity> typedQuery,
      final TaskFilePaginationAndSortingDTO taskFilePaginationAndSortingDTO) {
    typedQuery.setFirstResult(taskFilePaginationAndSortingDTO.getOffset());
    typedQuery.setMaxResults(taskFilePaginationAndSortingDTO.getLimit());
  }
}
