package com.aline.core.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Abstract class to create predicate for searching through
 * a given model.
 * @param <T> The model to search through.
 * @param <S> The search term type.
 */
@RequiredArgsConstructor
public abstract class SearchSpecification<T, S> implements Specification<T> {

    private final S searchTerm;

    protected S getSearchTerm() {
        return searchTerm;
    }

    @Override
    public abstract Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb);
}
