package com.freded.file.services;

import com.freded.dtos.PaginationAndSortingDTO;
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
     * Applies sorting to a CriteriaQuery based on the provided sorting parameters.
     * The sorting is done by using the field specified in `qParams.sortBy` and
     * the sorting direction specified in `qParams.sortOrder` (either "asc" for ascending or "desc" for descending).
     *
     * @param cb      The CriteriaBuilder used to construct the query and build expressions for sorting.
     * @param cbQuery The CriteriaQuery that needs to be modified with sorting order.
     * @param root    The root entity of the query (representing the main table or entity being queried).
     * @param qParams An object containing sorting parameters, including the field name (`sortBy`) and the order (`sortOrder`).
     *                This object must extend {@link PaginationAndSortingDTO}.
     * @param <T>     The type of the entity being queried, which will be sorted.
     * @param <Q>     The type of the sorting parameters object, which must extend {@link PaginationAndSortingDTO}.
     */
    public <T, Q extends PaginationAndSortingDTO> void sort(final CriteriaBuilder cb, final CriteriaQuery<T> cbQuery, final Root<T> root, final Q qParams) {

        Order order = DESC.equalsIgnoreCase(qParams.getSortOrder()) ? cb.desc(root.get(qParams.getSortBy())) : cb.asc(root.get(qParams.getSortBy()));

        cbQuery.orderBy(order);

    }

    /**
     * Paginate the entity based on the provided {@link PaginationAndSortingDTO} parameters.
     *
     * @param typedQuery the {@link TypedQuery} to apply pagination to.
     * @param qParams    the {@link PaginationAndSortingDTO} containing pagination options.
     * @param <T>        The type of the entity being queried, which will be sorted.
     * @param <Q>        The type of the pagination parameters object, which must extend {@link PaginationAndSortingDTO}.
     */
    public <T, Q extends PaginationAndSortingDTO> void paginate(final TypedQuery<T> typedQuery, final Q qParams) {
        typedQuery.setFirstResult(qParams.getOffset());
        typedQuery.setMaxResults(qParams.getLimit());
    }

}
