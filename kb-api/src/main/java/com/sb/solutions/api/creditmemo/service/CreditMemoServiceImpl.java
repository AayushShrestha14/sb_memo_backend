package com.sb.solutions.api.creditmemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.creditmemo.repository.spec.MemoSpecBuilder;
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
        User user = userService.getAuthenticated();
        if (creditMemo.getId() == null) {
            final CreditMemoStage stage = new CreditMemoStage();
            stage.setFromRole(user.getRole());
            stage.setToRole(user.getRole());
            stage.setFromUser(user);
            stage.setToUser(user);
            stage.setComment(DocAction.DRAFT.toString());
            stage.setDocAction(DocAction.DRAFT);
            creditMemo.setCurrentStage(stage);
            //set other initiator information dynamically

            creditMemo.setBranch(user.getBranch().get(0));
            if(user.getUsername() != null){
                StringBuilder fromUserName = new StringBuilder();
                fromUserName.append(user.getName()).append(" ").append("(").append(user.getRole().getRoleName()).append(")");
                creditMemo.setFromUser(fromUserName.toString());
            }
            user.getBranch().forEach(branch->{
                creditMemo.setBranchName(branch.getName());
            });
        }

        //set memo userFlow
        StringBuilder userFlow = new StringBuilder();
        creditMemo.getUserFlow().forEach( flow -> {
            /*filter userflow and set top index user to topUser and rest for CC:
            * to: top index user
            * CC: rest user */
            if(flow.getId() != creditMemo.getUserFlow().get(creditMemo.getUserFlow().size()-1).getId()) {
                userFlow.append(flow.getName()).append("/");
            }
        });

        //set userFlow CC
        if(creditMemo.getUserFlow().size() != 1){
            userFlow.replace(userFlow.length()-1, userFlow.length()," ");
        }

        //set cc
        StringBuilder memoCc = new StringBuilder();
        creditMemo.getMemoCc().forEach( flow -> {
            if(flow.getId() != creditMemo.getMemoCc().get(creditMemo.getMemoCc().size()-1).getId()) {
                memoCc.append(flow.getName()).append("/");
            }
        });
        if(creditMemo.getMemoCc().size() != 1){
            memoCc.replace(memoCc.length()-1, memoCc.length()," ");
        }
        creditMemo.setToUser(userFlow.toString());
        return repository.save(creditMemo);
    }

    @Override
    public Page<CreditMemo> findAllPageable(Object t, Pageable pageable) {
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
        User currentUser = userService.getAuthenticated();
        if(!Objects.isNull(currentUser) && !currentUser.getRole().getRoleName().equals("admin")){
            search.put("currentPossessionUserId", String.valueOf(currentUser.getId()));
        }

        search.values().removeIf(Objects::isNull);
//        User user = userService.getAuthenticated();
//        if(user.getBranch() != null) {
//            for(Branch userBranch: user.getBranch()){
//                search.put("branchName", userBranch.getName());
//            }
//
//        }
//        String branchAccess = userService.getRoleAccessFilterByBranch().stream()
//                .map(Object::toString).collect(Collectors.joining(","));
//        if (search.containsKey("branchIds")) {
//            branchAccess = search.get("branchIds");
//        }
//        search.put("branchIds", branchAccess);
//
//        User u = userService.getAuthenticated();
//
//          if(search.containsKey("documentStatus") &&
//                  search.get("documentStatus").equalsIgnoreCase(DocStatus.PENDING.toString())){
//              search.put("currentStage.toRole.id", u.getRole() == null ? null :
//                      Objects.requireNonNull(u.getRole().getId()).toString());
//              search.put("currentStage.toUser.id", Objects.requireNonNull(u.getId()).toString());
//          }
//
//        search.values().removeIf(Objects::isNull);
        MemoSpecBuilder builder = new MemoSpecBuilder(search);
        return repository.findAll(builder.build(), pageable);
    }

    @Override
    public List<CreditMemo> findByBranch() {
        User user = userService.getAuthenticated();
        List<CreditMemo> memoByBranch = new ArrayList<>();
        for (Branch b : user.getBranch()) {
            memoByBranch.addAll(repository.findByBranch(b));
        }
        return memoByBranch;
    }

    @Override
    public Page<CreditMemo> findAllAppriseMemo(Object t, Pageable pageable) {
        final ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> search = objectMapper.convertValue(t, Map.class);
        User currentUser = userService.getAuthenticated();
        if (!Objects.isNull(currentUser) && !currentUser.getRole().getRoleName().equals("admin")) {
                        search.put("memoCc", String.valueOf(currentUser.getId()));
        }
        search.values().removeIf(Objects::isNull);
        MemoSpecBuilder builder = new MemoSpecBuilder(search);
        return repository.findAll(builder.build(), pageable);
    }



}
