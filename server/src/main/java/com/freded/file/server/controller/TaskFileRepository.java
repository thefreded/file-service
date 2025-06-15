package com.freded.file.server.controller;

import com.freded.dtos.TaskFilePaginationAndSortingDTO;
import com.freded.file.server.entity.TaskFileEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TaskFileRepository {

  @Inject EntityManager entityManager;

  @Inject PaginationAndSortingService paginationAndSortingService;

  public TaskFileEntity getByUploadedByAndId(final String uploadedBy, final UUID taskFileId) {

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<TaskFileEntity> cq = cb.createQuery(TaskFileEntity.class);
    Root<TaskFileEntity> taskFile = cq.from(TaskFileEntity.class);

    // Add conditions
    Predicate idPredicate = cb.equal(taskFile.get("id"), taskFileId);
    Predicate createdByPredicate = cb.equal(taskFile.get("uploadedBy"), uploadedBy);
    cq.where(cb.and(idPredicate, createdByPredicate));

    try {
      return entityManager.createQuery(cq).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public List<TaskFileEntity> readAll(
      final String uploadedBy,
      final String taskId,
      final TaskFilePaginationAndSortingDTO taskFilePaginationAndSortingDTO) {

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();

    CriteriaQuery<TaskFileEntity> cbQuery = cb.createQuery(TaskFileEntity.class);

    Root<TaskFileEntity> root = cbQuery.from(TaskFileEntity.class);

    ParameterExpression<String> taskIdParam = cb.parameter(String.class, "taskId");
    ParameterExpression<String> uploadedByParam = cb.parameter(String.class, "uploadedBy");

    cbQuery
        .select(root)
        .where(cb.and(cb.equal(root.get("taskId"), taskIdParam), cb.equal(root.get("uploadedBy"), uploadedByParam)));

    // Apply sorting based on the parameters provided in taskFilePaginationAndSortingDTO.
    paginationAndSortingService.sort(cb, cbQuery, root, taskFilePaginationAndSortingDTO);

    TypedQuery<TaskFileEntity> typedQuery = entityManager.createQuery(cbQuery);

    typedQuery.setParameter("uploadedBy", uploadedBy);
    typedQuery.setParameter("taskId", taskId);

    paginationAndSortingService.paginate(typedQuery, taskFilePaginationAndSortingDTO);

    return typedQuery.getResultList();
  }
}
