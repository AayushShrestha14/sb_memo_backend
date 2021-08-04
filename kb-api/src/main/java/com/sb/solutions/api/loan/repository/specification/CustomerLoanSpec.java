package com.sb.solutions.api.loan.repository.specification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.core.constant.AppConstant;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.core.enums.LoanType;

/**
 * @author Rujan Maharjan on 6/8/2019
 */
public class CustomerLoanSpec implements Specification<CustomerLoan> {

    private static final String FILTER_BY_LOAN = "loanConfigId";
    private static final String FILTER_BY_DOC_STATUS = "documentStatus";
    private static final String FILTER_BY_CURRENT_USER_ROLE = "currentUserRole";
    private static final String FILTER_BY_TO_USER = "toUser";
    private static final String FILTER_BY_BRANCH = "branchIds";
    private static final String FILTER_BY_CURRENT_STAGE_DATE = "currentStageDate";
    private static final String FILTER_BY_TYPE = "loanNewRenew";
    private static final String FILTER_BY_NOTIFY = "notify";
    private static final String FILTER_BY_CUSTOMER_NAME = "customerName";
    private static final String FILTER_BY_COMPANY_NAME = "companyName";
    private static final String FILTER_BY_CUSTOMER_CITIZENSHIP_ISSUED_DATE = "citizenshipIssuedDate";
    private static final String FILTER_BY_CITIZENSHIP_ISSUED_DISTRICT = "citizenshipIssuedDistrict";
    private static final String FILTER_BY_CITIZENSHIP_NUMBER = "citizenshipNumber";

    private final String property;
    private final String value;

    public CustomerLoanSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<CustomerLoan> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {

        switch (property) {
            case FILTER_BY_DOC_STATUS:
                return criteriaBuilder.equal(root.get(property), DocStatus.valueOf(value));

            case FILTER_BY_LOAN:
                return criteriaBuilder
                    .and(criteriaBuilder.equal(root.join("loan").get("id"), Long.valueOf(value)));

            case FILTER_BY_CURRENT_USER_ROLE:
                return criteriaBuilder
                    .equal(root.join("currentStage", JoinType.LEFT).join("toRole").get("id"),
                        Long.valueOf(value));

            case FILTER_BY_BRANCH:
                Pattern pattern = Pattern.compile(",");
                List<Long> list = pattern.splitAsStream(value)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
                Expression<String> exp = root.join("branch").get("id");
                Predicate predicate = exp.in(list);
                return criteriaBuilder.and(predicate);

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

            case FILTER_BY_TO_USER:
                return criteriaBuilder
                    .equal(
                        root.join("currentStage", JoinType.LEFT).join(FILTER_BY_TO_USER).get("id"),
                        Long.valueOf(value));

            case FILTER_BY_TYPE:
                return criteriaBuilder.equal(root.get("loanType"), LoanType.valueOf(value));

            case FILTER_BY_NOTIFY:
                Predicate notifyPredicate = criteriaBuilder
                    .equal(root.get(FILTER_BY_NOTIFY), Boolean.valueOf(value));
                Predicate notedByPredicate = criteriaBuilder.isNull(root.get("notedBy"));
                return criteriaBuilder.and(notifyPredicate, notedByPredicate);

            case FILTER_BY_CUSTOMER_NAME:
                return criteriaBuilder
                    .like(criteriaBuilder
                            .lower(root.join("customerInfo").get(FILTER_BY_CUSTOMER_NAME)),
                        "%" + value.toLowerCase() + "%");

            case FILTER_BY_COMPANY_NAME:
                return criteriaBuilder
                    .like(criteriaBuilder
                            .lower(root.join("companyInfo").get(FILTER_BY_COMPANY_NAME)),
                        "%" + value.toLowerCase() + "%");

            case FILTER_BY_CUSTOMER_CITIZENSHIP_ISSUED_DATE:
                try {
                    Date date = new SimpleDateFormat(AppConstant.YYYY_MM_DD).parse(value);
                    return criteriaBuilder.equal(
                        root.join("customerInfo").get(FILTER_BY_CUSTOMER_CITIZENSHIP_ISSUED_DATE),
                        date);
                } catch (ParseException e) {
                    return null;
                }

            case FILTER_BY_CITIZENSHIP_NUMBER:
                return criteriaBuilder
                    .equal(root.join("customerInfo").get(FILTER_BY_CITIZENSHIP_NUMBER), value);

            case FILTER_BY_CITIZENSHIP_ISSUED_DISTRICT:
                return criteriaBuilder.equal(
                    root.join("customerInfo").join(FILTER_BY_CITIZENSHIP_ISSUED_DISTRICT).get("id"),
                    Long.valueOf(value));

            default:
                return null;
        }
    }

}
