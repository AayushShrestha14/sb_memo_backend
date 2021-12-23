package com.sb.solutions.api.template.repository.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import com.sb.solutions.api.template.entity.Template;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.core.enums.Status;

public class TemplateSpec implements Specification<Template> {

    private static final String FILTER_BY_NAME = "name";
    private static final String FILTER_BY_STATUS = "status";

    private final String property;
    private final String value;

    public TemplateSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
       switch (property){
           case FILTER_BY_NAME:
               return criteriaBuilder.like(criteriaBuilder.lower(root.get(property)),
                   "%"+value.toLowerCase()+"%");

           case FILTER_BY_STATUS:
               return criteriaBuilder.equal(root.get(property), Status.valueOf(value));
           default:
               return null;
       }
    }
}
