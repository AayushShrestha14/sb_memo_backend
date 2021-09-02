package com.sb.solutions.api.creditmemo.repository.spec;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.gson.Gson;
import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.creditmemo.entity.CreditMemo;
import com.sb.solutions.core.enums.DocStatus;

/**
 * @author Elvin Shrestha on 7/7/2020
 */
public class CreditMemoSpec implements Specification<CreditMemo> {

    private static final String FILTER_BY_SUBJECT = "subject";
    private static final String FILTER_BY_DOC_STATUS = "documentStatus";
    private static final String FILTER_BY_BRANCH = "branchIds";
    private static final String FILTER_BY_CUSTOMER_NAME = "customerInfoId";
    private static final String FILTER_BY_TYPE_NAME = "type.name";
    private static final String FILTER_BY_TYPE_ID = "CreditMemoTypeId";
    private static final String FILTER_BY_CURRENT_STAGE_TO_ROLE = "currentStage.toRole.id";
    private static final String FILTER_BY_CURRENT_STAGE_TO_USER = "currentStage.toUser.id";
    private static final String FILTER_BY_REFERENCE_NUMBER = "referenceNumber";
    private static final String FILTER_BY_LOAN_FACILITY = "loanId";
    private static final String FILTER_BY_CURRENT_STAGE_DATE = "currentStageDate";
    private static final String FILTER_BY_LOAN_ASSOCIATED = "loanAssociated";

    private final String property;
    private final String value;

    public CreditMemoSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<CreditMemo> root, CriteriaQuery<?> query,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {
            case FILTER_BY_SUBJECT:
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(FILTER_BY_SUBJECT)),
                    "%" + value.toLowerCase() + "%");
            case FILTER_BY_DOC_STATUS:
                return criteriaBuilder.equal(root.get(property), DocStatus.valueOf(value));
            case FILTER_BY_BRANCH:
                Pattern pattern = Pattern.compile(",");
                List<Long> list = pattern.splitAsStream(value)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
                Expression<String> exp = root.join("branch").get("id");
                Predicate predicate = exp.in(list);
                return criteriaBuilder.and(predicate);
            case FILTER_BY_TYPE_ID:
                return criteriaBuilder.equal(root.join("type").get("id"), Long.parseLong(value));
            case FILTER_BY_TYPE_NAME:
                return criteriaBuilder.like(criteriaBuilder.lower(root.join("type").get("name")),
                    "%" + value.toLowerCase() + "%");
            case FILTER_BY_CURRENT_STAGE_TO_ROLE:
                return criteriaBuilder
                    .equal(root.join("currentStage", JoinType.LEFT).join("toRole").get("id"),
                        Long.valueOf(value));
            case FILTER_BY_CURRENT_STAGE_TO_USER:
                return criteriaBuilder.equal(
                        root.join("currentStage", JoinType.LEFT).join("toUser").get("id"),
                        Long.valueOf(value));
            case FILTER_BY_CUSTOMER_NAME:
                return criteriaBuilder
                        .like(criteriaBuilder.lower(root.join("customerLoan").get("customerInfo").get("customerName")),
                                "%" + value.toLowerCase() + "%");
            case FILTER_BY_REFERENCE_NUMBER:
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(FILTER_BY_REFERENCE_NUMBER)),
                        "%" + value.toLowerCase() + "%");
            case FILTER_BY_LOAN_FACILITY:
                return criteriaBuilder
                        .equal(root.join("customerLoan", JoinType.LEFT).join("loan").get("id"),
                                Long.valueOf(value));
            case FILTER_BY_CURRENT_STAGE_DATE:
                Gson gson = new Gson();
                Map dates = gson.fromJson(value, Map.class);
                try {
                    return criteriaBuilder.between(root.get("createdAt"),
                            new SimpleDateFormat("MM/dd/yyyy")
                                    .parse(String.valueOf(dates.get("startDate"))),
                            new SimpleDateFormat("MM/dd/yyyy")
                                    .parse(String.valueOf(dates.get("endDate"))));
                } catch (ParseException e) {
                    return null;
                }
            case FILTER_BY_LOAN_ASSOCIATED:
                if(value.equalsIgnoreCase("true")) {
                    return criteriaBuilder.equal(root.get(property), true);
                }else if(value.equalsIgnoreCase("false")){
                    return criteriaBuilder.equal(root.get(property), false);
                }
            default:
                return null;
        }
    }
}
