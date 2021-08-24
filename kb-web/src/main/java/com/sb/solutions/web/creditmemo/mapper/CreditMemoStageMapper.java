package com.sb.solutions.web.creditmemo.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sb.solutions.api.creditmemo.entity.CreditMemo;
import com.sb.solutions.api.creditmemo.entity.CreditMemoStage;
import com.sb.solutions.api.loan.dto.LoanStageDto;
import com.sb.solutions.api.rolePermissionRight.entity.Role;
import com.sb.solutions.api.rolePermissionRight.service.RoleService;
import com.sb.solutions.api.stage.entity.Stage;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.web.common.stage.dto.StageDto;

/**
 * @author Elvin Shrestha on 7/7/2020
 */
@Component
public class CreditMemoStageMapper {

    private static final Logger logger = LoggerFactory.getLogger(CreditMemoStageMapper.class);
    private final UserService userService;
    private final RoleService roleService;

    public CreditMemoStageMapper(
        UserService userService,
        RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    public CreditMemo action(StageDto actionDto, CreditMemo creditMemo) {
        // Adds current stage into previous stage list
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_EMPTY);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        List<LoanStageDto> previousStages = creditMemo.getPreviousStages();
        List<String> previousStageList = new ArrayList<>();
        CreditMemoStage creditMemoStage = new CreditMemoStage();

        if (creditMemo.getCurrentStage() != null) {
            creditMemoStage = creditMemo.getCurrentStage();
            Map<String, String> tempCurrentStage = objectMapper
                .convertValue(creditMemo.getCurrentStage(), Map.class);
            try {
                previousStages.forEach(s -> {
                    try {
                        Map<String, String> stageString = objectMapper.convertValue(s, Map.class);
                        previousStageList.add(objectMapper.writeValueAsString(stageString));
                    } catch (JsonProcessingException e) {
                        logger.error("Failed to handle Credit Memo JSON data {}", e.getMessage());
                    }
                });
                previousStageList.add(objectMapper.writeValueAsString(tempCurrentStage));
            } catch (JsonProcessingException e) {
                logger.error("Failed to get credit memo stage data {}", e.getMessage());
            }
        }

        creditMemo.setPreviousStageList(previousStageList.toString());
        creditMemo.setDocumentStatus(actionDto.getDocumentStatus());
        this.updateStage(actionDto, previousStages, creditMemoStage, creditMemo);
        creditMemo.setCurrentStage(creditMemoStage);
        return creditMemo;
    }

    private void updateStage(StageDto stageDto, List<LoanStageDto> previousList,
        CreditMemoStage currentStage, CreditMemo creditMemo) {

        User currentUser = userService.getAuthenticated();
        currentStage.setFromUser(currentUser);
        currentStage.setFromRole(currentUser.getRole());
        currentStage.setComment(stageDto.getComment());
        currentStage.setDocAction(stageDto.getDocAction());

        switch (stageDto.getDocAction()) {
            case FORWARD:
                Role r = roleService.findOne(stageDto.getToRole().getId());
                currentStage.setToRole(r);
                User u = new User();
                u.setId(stageDto.getToUser().getId());
                currentStage.setToUser(u);
                logger.info("Forward credit memo with stage: {}", currentStage);
                break;

            case BACKWARD:
                User user = userService.getAuthenticated();
                int currentIndex = creditMemo.getUserFlow().lastIndexOf(user);
                if(currentIndex >0 && currentIndex != 0) {
                    User toUser = creditMemo.getUserFlow().get(currentIndex - 1);
                    currentStage.setToUser(toUser);
                    currentStage.setToRole(toUser.getRole());
                }
                else {
                    if (previousList.isEmpty()) {
                        currentStage.setToUser(currentStage.getFromUser());
                        currentStage.setToRole(currentStage.getFromRole());
                    } else {
                        memoBackward(previousList,currentStage,creditMemo);
                    }
                }
                break;

            case BACKWARD_INITIATOR:
                if (previousList.isEmpty()) {
                        currentStage.setToUser(currentStage.getFromUser());
                        currentStage.setToRole(currentStage.getFromRole());
                    } else {
                        memoBackward(previousList,currentStage,creditMemo);
                    }

                break;

            case APPROVED:
            case REJECT:
                currentStage.setToRole(currentUser.getRole());
                currentStage.setToUser(currentUser);
                logger.info("{} credit memo stage: {}", stageDto.getDocAction(), currentStage);
                break;
        }
    }

    private void memoBackward(List<LoanStageDto> previousList,
                              CreditMemoStage currentStage,
                              CreditMemo creditMemo){
        if (previousList.isEmpty()) {
            currentStage.setToUser(currentStage.getFromUser());
            currentStage.setToRole(currentStage.getFromRole());
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(Include.NON_EMPTY);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            for (Object obj : previousList) {
                Stage maker = objectMapper.convertValue(obj, Stage.class);
                if (maker.getFromUser().getId().equals(currentStage.getCreatedBy())) {
                    currentStage.setToRole(maker.getFromRole());

                    try {
                        final List<User> users = userService
                                .findByRoleAndBranchId(maker.getFromRole().getId(),
                                        creditMemo.getBranch().getId());
                        final List<Long> userIdList = users.stream().map(User::getId)
                                .collect(Collectors.toList());
                        if (userIdList.contains(currentStage.getCreatedBy())) {
                            java.util.Optional<User> userOptional = users.stream().
                                    filter(p -> p.getId().equals(currentStage.getCreatedBy())).
                                    findFirst();
                            currentStage.setToUser(
                                    objectMapper.convertValue(userOptional.get(), User.class));
                        } else {
                            currentStage
                                    .setToUser(
                                            objectMapper.convertValue(users.get(0), User.class));
                        }
                    } catch (Exception e) {
                        logger.error("Error occurred while mapping credit memo stage", e);
                        throw new RuntimeException("Error while performing the action");
                    }
                }
            }
        }

    }

}
