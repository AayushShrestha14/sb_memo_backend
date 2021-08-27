package com.sb.solutions.api.creditmemo.repository.spec;

import com.sb.solutions.api.creditmemo.entity.CreditMemoType;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.core.enums.Status;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class MemoTypeSpec implements Specification<CreditMemoType> {
    private static final String FILTER_BY_NAME = "name";
    private static final String FILTER_BY_CATEGORY = "category";
    private static final String FILTER_BY_STATUS = "status";


    private final String property;
    private final String value;

    public MemoTypeSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<CreditMemoType> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
       switch (property){
           case FILTER_BY_NAME:
               return criteriaBuilder.like(criteriaBuilder.lower(root.get(property)),
                       "%"+value.toLowerCase()+"%");

           case FILTER_BY_CATEGORY:
               return criteriaBuilder.equal(root.join("category").get("id"), Long.parseLong(value));

           case FILTER_BY_STATUS:
               return criteriaBuilder.equal(root.get(property), Status.valueOf(value));

           default:
               return null;
       }
    }
}
