package com.sb.solutions.api.creditmemo.repository.spec;

import com.google.gson.Gson;
import com.sb.solutions.api.creditmemo.entity.CreditMemo;
import com.sb.solutions.core.enums.DocStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CreditMemoTypeSpec implements Specification<CreditMemo> {
    private static final String FILTER_BY_TYPE_ID = "CreditMemoTypeId";
    private static final String FILTER_BY_CURRENT_STAGE_DATE = "currentStageDate";
    private static final String FILTER_BY_LOAN_ASSOCIATED = "loanAssociated";
    private static final String FILTER_BY_REFERENCE_NUMBER = "referenceNumber";
    private static final String FILTER_BY_DOC_STATUS = "documentStatus";
    private static final String FILTER_BY_BRANCH_NAME = "branchName";

    private final String property;
    private final String value;

    public CreditMemoTypeSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }
    @Override
    public Predicate toPredicate(Root<CreditMemo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

            switch (property) {
                case FILTER_BY_REFERENCE_NUMBER:
                    return criteriaBuilder.like(criteriaBuilder.lower(root.get(FILTER_BY_REFERENCE_NUMBER)),
                            "%" + value.toLowerCase() + "%");
                case FILTER_BY_TYPE_ID:
                    return criteriaBuilder.equal(root.join("type").get("id"), Long.parseLong(value));
                case FILTER_BY_LOAN_ASSOCIATED:
                    return criteriaBuilder.equal(root.get(property), false);
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
                case FILTER_BY_DOC_STATUS:
                    return criteriaBuilder.equal(root.get(property), DocStatus.valueOf(value));

                case FILTER_BY_BRANCH_NAME:
                    return criteriaBuilder.like(criteriaBuilder.lower(root.get(FILTER_BY_BRANCH_NAME)),
                            "%" + value.toLowerCase() + "%");

                default:
                    return null;
            }
        }

}
