package com.sb.solutions.api.creditmemo.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.sb.solutions.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sb.solutions.api.loan.dto.LoanStageDto;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.DocStatus;

/**
 * @author Elvin Shrestha on 7/7/2020
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreditMemo extends BaseEntity<Long> {

    private static final Logger logger = LoggerFactory.getLogger(CreditMemo.class);

    @OneToOne
    @JoinColumn(name = "credit_memo_type_id")
    @NotNull
    private CreditMemoType type;

    @NotNull
    private String referenceNumber;

    @NotNull
    private String subject;

    @NotNull
    private String content;

    private boolean loanAssociated;

    private String toUser;

    private String branchName;

    private String fromUser;

    private DocStatus documentStatus = DocStatus.PENDING;

    @OneToOne(cascade = CascadeType.ALL)
    private CreditMemoStage currentStage;

    private String previousStageList;

    @ManyToOne
    private CustomerLoan customerLoan;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "credit_memo_documents_path")
    private List<CreditMemoDocument> documents;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "credit_memo_memo_type_documents_path")
    private List<CreditMemoMemoTypeDocuments> memoTypeDocuments;


    @Transient
    private List<LoanStageDto> previousStages;

    @Transient
    private List<LoanStageDto> distinctPreviousList;

    @ManyToMany
    private List<User> userFlow;

    public List<LoanStageDto> getPreviousStages() {
        if (this.getPreviousStageList() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(Include.NON_EMPTY);
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            try {
                this.previousStages = objectMapper
                    .readValue(this.getPreviousStageList(),
                        typeFactory.constructCollectionType(List.class, LoanStageDto.class));
            } catch (IOException e) {
                logger.error("Error parsing previousStageList {}", e.getLocalizedMessage());
            }
        } else {
            this.previousStages = new ArrayList<>();
        }
        return this.previousStages;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public List<LoanStageDto> getDistinctPreviousList() {
        Collection<LoanStageDto> list =
            CollectionUtils.isEmpty(this.getPreviousStages()) || CollectionUtils
                .isEmpty(this.previousStages) ? new ArrayList<>() : this.getPreviousStages();
        return list.stream()
            .filter(distinctByKey(
                p -> (p.getToUser() == null ? p.getToRole().getId() : p.getToUser().getId())))
            .filter(p -> !p.getToUser().getIsDefaultCommittee())
            .collect(Collectors.toList());
    }

}

