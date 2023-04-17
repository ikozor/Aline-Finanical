package com.aline.core.util;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.persistence.TupleElement;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public abstract class NestedSearchSpecification<T> extends SearchSpecification<T, String> {

    public NestedSearchSpecification(String searchTerm) {
        super(searchTerm);
    }

    /**
     * @return A stream of lower case search strings
     */
    private Stream<String> getSearchTerms() {
        return Arrays.stream(getSearchTerm().split("[\\s,]"))
                .map(String::toLowerCase);
    }

    public abstract List<Class<?>> getNestedEntityTypes();

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

        Predicate[] topLevelAttributes = getSearchTerms()
                .flatMap(searchTerm -> root.getModel().getAttributes().stream()
                        .filter(attribute -> attribute.getJavaType().getSimpleName().equals("String"))
                        .map(Attribute::getName)
                        .map(attributeName -> cb.like(cb.lower(root.get(attributeName)), "%" + searchTerm + "%")))
                .toArray(Predicate[]::new);

        val nextAttributes = getSearchTerms()
                .flatMap(searchTerm -> root.getModel().getAttributes().stream()
                        .filter(attribute -> getNestedEntityTypes().contains(attribute.getJavaType()))
                        .map(Attribute::getName)
                        .map(root::join)
                        .map(From::getJoins)
                        .flatMap(joins -> joins.stream().map(Join::getAttribute))
                        .map(Attribute::getName)
                ).collect(Collectors.joining(", "));

        log.info(nextAttributes);
        return cb.or(topLevelAttributes);
    }
}
