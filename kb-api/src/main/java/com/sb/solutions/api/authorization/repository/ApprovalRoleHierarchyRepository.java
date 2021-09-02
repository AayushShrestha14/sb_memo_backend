package com.sb.solutions.api.authorization.repository;

import com.sb.solutions.api.authorization.entity.ApprovalRoleHierarchy;
import com.sb.solutions.core.enums.ApprovalType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalRoleHierarchyRepository extends JpaRepository<ApprovalRoleHierarchy,Long> {
    List<ApprovalRoleHierarchy> findAllByApprovalTypeEqualsAndRefId(
            ApprovalType approvalType,
            Long refId);
    boolean existsByApprovalTypeAndRefIdAndRoleId(ApprovalType approvalType,Long refId,Long roleId);

}
