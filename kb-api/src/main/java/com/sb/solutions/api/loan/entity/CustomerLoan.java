package com.sb.solutions.api.loan.entity;

import static com.sb.solutions.core.constant.AppConstant.YYYY_MM_DD;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import com.sb.solutions.api.approvallimit.emuns.LoanApprovalType;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.companyInfo.model.entity.CompanyInfo;
import com.sb.solutions.api.customer.entity.Customer;
import com.sb.solutions.api.dms.dmsloanfile.entity.DmsLoanFile;
import com.sb.solutions.api.loan.LoanStage;
import com.sb.solutions.api.loan.dto.LoanStageDto;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.core.enums.LoanType;
import com.sb.solutions.core.enums.Priority;

/**
 * @author Rujan Maharjan on 6/4/2019
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerLoan extends BaseEntity<Long> {

    @ManyToOne
    private Customer customerInfo;

    @OneToOne
    private LoanConfig loan;

    @ManyToOne
    private CompanyInfo companyInfo;

    private LoanType loanType = LoanType.NEW_LOAN;

    private DocStatus documentStatus = DocStatus.PENDING;

    private LoanApprovalType loanCategory;
    @ManyToOne
    private DmsLoanFile dmsLoanFile;

//    @OneToMany
//    @JoinColumn(name = "customer_loan_id")


    @OneToOne(cascade = CascadeType.ALL)
    private LoanStage currentStage;

    private Priority priority;

    private Long parentId;

    private Long childId;

    private Boolean isCloseRenew;

    @Transient
    private List previousList;

    @OneToOne
    private Branch branch;

    private Boolean notify;

    private Long notedBy;

    private String offerLetterUrl;


    @Lob
    private String previousStageList;

    private Boolean isValidated = false;

    @Transient
    private Boolean pulled = false;

    @Transient
    private List<LoanStage> distinctPreviousList;

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public List<LoanStageDto> getPreviousList() {
        if (this.getPreviousStageList() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat(YYYY_MM_DD));
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            try {
                this.previousList = objectMapper.readValue(this.getPreviousStageList(),
                    typeFactory.constructCollectionType(List.class, LoanStageDto.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.previousList = new ArrayList();
        }
        return this.previousList;
    }

    public List<LoanStageDto> getDistinctPreviousList() {
        Collection<LoanStageDto> list =
            CollectionUtils.isEmpty(this.getPreviousList()) || CollectionUtils
                .isEmpty(this.previousList) ? new ArrayList<>() : this.getPreviousList();
        return list.stream()
            .filter(distinctByKey(
                p -> (p.getToUser() == null ? p.getToRole().getId() : p.getToUser().getId())))
            .filter(p -> !p.getToUser().getIsDefaultCommittee())
            .collect(Collectors.toList());
    }
}
