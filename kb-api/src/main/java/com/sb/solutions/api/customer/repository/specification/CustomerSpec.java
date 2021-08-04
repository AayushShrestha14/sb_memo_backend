package com.sb.solutions.api.customer.repository.specification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.customer.entity.Customer;
import com.sb.solutions.core.constant.AppConstant;

public class CustomerSpec implements Specification<Customer> {

    private static final String FILTER_BY_CITIZENSHIP_NUMBER = "citizenshipNumber";
    private static final String FILTER_BY_NAME = "customerName";
    private static final String FILTER_BY_CITIZENSHIP_ISSUED_DATE = "citizenshipIssuedDate";
    private static final String FILTER_BY_CITIZENSHIP_ISSUED_DISTRICT = "citizenshipIssuedDistrict";

    private final String property;
    private final String value;

    public CustomerSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {

        switch (property) {
            case FILTER_BY_CITIZENSHIP_NUMBER:
                return criteriaBuilder.equal(root.get(property), value);
            case FILTER_BY_CITIZENSHIP_ISSUED_DATE:
                try {
                    Date date = new SimpleDateFormat(AppConstant.YYYY_MM_DD).parse(value);
                    return criteriaBuilder.equal(root.get(FILTER_BY_CITIZENSHIP_ISSUED_DATE), date);
                } catch (ParseException e) {
                    return null;
                }
            case FILTER_BY_NAME:
                return criteriaBuilder.like(root.get(FILTER_BY_NAME), "%" + value + "%");
            case FILTER_BY_CITIZENSHIP_ISSUED_DISTRICT:
                return criteriaBuilder
                    .equal(root.join(FILTER_BY_CITIZENSHIP_ISSUED_DISTRICT).get("id"),
                        Long.valueOf(value));
            default:
                return null;
        }
    }
}
