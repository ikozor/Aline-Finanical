package com.aline.core.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import java.util.Arrays;

/**
 * The SearchSpecification class is used to return
 * a specified list of elements based on the search term.
 * The search term searches through all columns of the entity
 * that are strings and if at least one term is matched in any
 * of the string columns, it will return a list with that entity
 * in it.
 * @param <T> The entity type that a search is being applied to.
 */
public class SimpleSearchSpecification<T> extends SearchSpecification<T, String> {

    public SimpleSearchSpecification(String searchTerm) {
        super(searchTerm);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        String[] searchTerms = getSearchTerm().split("[\\s,]");

        Predicate[] predicates = Arrays.stream(searchTerms)
                .map(String::toLowerCase)
                .flatMap(searchTerm -> root.getModel().getAttributes().stream()
                        .filter(attribute -> attribute.getJavaType() == String.class)
                        .map(Attribute::getName)
                        .map(attributeName -> cb.like(cb.lower(root.get(attributeName)), "%" + searchTerm + "%")))
                .toArray(Predicate[]::new);

        return cb.or(predicates);
    }
}
