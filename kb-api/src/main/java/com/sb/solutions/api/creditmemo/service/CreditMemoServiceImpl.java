package com.sb.solutions.api.creditmemo.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.creditmemo.repository.spec.CreditMemoTypeSpecBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.creditmemo.entity.CreditMemo;
import com.sb.solutions.api.creditmemo.entity.CreditMemoStage;
import com.sb.solutions.api.creditmemo.repository.CreditMemoRepository;
import com.sb.solutions.api.creditmemo.repository.spec.CreditMemoSpecBuilder;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.enums.DocAction;
import com.sb.solutions.core.enums.DocStatus;

/**
 * @author Elvin Shrestha on 7/7/2020
 */
@Service
public class CreditMemoServiceImpl implements CreditMemoService {

    private final CreditMemoRepository repository;
    private final UserService userService;

    public CreditMemoServiceImpl(
        CreditMemoRepository repository,
        UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public List<CreditMemo> findAll() {
        return repository.findAll();
    }

    @Override
    public CreditMemo findOne(Long id) {
        return repository.getOne(id);
    }

    @Override
    public CreditMemo save(CreditMemo creditMemo) {
//        Preconditions
//            .checkNotNull(creditMemo.getCustomerLoan(), "Missing Associated Customer Loan");
        if (creditMemo.getId() == null) {
            User user = userService.getAuthenticated();
            final CreditMemoStage stage = new CreditMemoStage();
            stage.setFromRole(user.getRole());
            stage.setToRole(user.getRole());
            stage.setFromUser(user);
            stage.setToUser(user);
            stage.setComment(DocAction.DRAFT.toString());
            stage.setDocAction(DocAction.DRAFT);
            creditMemo.setCurrentStage(stage);
        }
        return repository.save(creditMemo);
    }

    @Override
    public Page<CreditMemo> findAllPageable(Object t, Pageable pageable) {
        final ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> search = objectMapper.convertValue(t, Map.class);
        String branchAccess = userService.getRoleAccessFilterByBranch().stream()
            .map(Object::toString).collect(Collectors.joining(","));
//        if (search.containsKey("branchIds")) {
//            branchAccess = search.get("branchIds");
//        }
//        search.put("branchIds", branchAccess);
        User u = userService.getAuthenticated();
        if (search.containsKey("documentStatus")
            && search.get("documentStatus").equalsIgnoreCase(DocStatus.PENDING.toString())) {
            search.put("currentStage.toRole.id", u.getRole() == null ? null :
                Objects.requireNonNull(u.getRole().getId()).toString());
            search.put("currentStage.toUser.id", Objects.requireNonNull(u.getId()).toString());
        }
        search.values().removeIf(Objects::isNull);
        CreditMemoSpecBuilder builder = new CreditMemoSpecBuilder(search);
        return repository.findAll(builder.build(), pageable);
    }

    @Override
    public List<CreditMemo> saveAll(List<CreditMemo> list) {
        return repository.saveAll(list);
    }

    @Override
    public CreditMemo action(CreditMemo creditMemo) {
        return repository.save(creditMemo);
    }

    @Override
    public Map<String, Integer> statusCount() {
        User u = userService.getAuthenticated();
        List<Long> branchAccess = userService.getRoleAccessFilterByBranch();
        return repository.statusCount();
    }

    @Override
    public Page<CreditMemo> findAllPageableForLoanAssociated(Object t, Pageable pageable) {
        final ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> search = objectMapper.convertValue(t, Map.class);
        String branchAccess = userService.getRoleAccessFilterByBranch().stream()
                .map(Object::toString).collect(Collectors.joining(","));
        if (search.containsKey("branchIds")) {
            branchAccess = search.get("branchIds");
        }
        search.put("branchIds", branchAccess);
        User u = userService.getAuthenticated();
        if (search.containsKey("documentStatus")
                && search.get("documentStatus").equalsIgnoreCase(DocStatus.PENDING.toString())) {
            search.put("currentStage.toRole.id", u.getRole() == null ? null :
                    Objects.requireNonNull(u.getRole().getId()).toString());
            search.put("currentStage.toUser.id", Objects.requireNonNull(u.getId()).toString());
        }
        search.values().removeIf(Objects::isNull);

        CreditMemoSpecBuilder builder = new CreditMemoSpecBuilder(search);
            return repository.findAll(builder.build(), pageable);

    }


    @Override
    public Page<CreditMemo> findAllMemoTypePageableWithFilter(Object t, Pageable pageable) {
        final ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> search = objectMapper.convertValue(t, Map.class);
        User user = userService.getAuthenticated();
        if(user.getBranch() != null) {
            for(Branch userBranch: user.getBranch()){
                search.put("branchName", userBranch.getName());
            }

        }
        CreditMemoTypeSpecBuilder builder = new CreditMemoTypeSpecBuilder(search);
        return repository.findAll(builder.build(), pageable);
    }


}
