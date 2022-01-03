package com.sb.solutions.api.creditMemoCategory.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.template.entity.Template;
import com.sb.solutions.core.enums.Status;

public class MemoCategorySpec implements Specification<Template> {

    private static final String FILTER_BY_NAME = "name";

    private final String property;
    private final String value;

    public MemoCategorySpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {
            case FILTER_BY_NAME:
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(property)),
                    "%" + value.toLowerCase() + "%");
            default:
                return null;
        }
    }
}
