package com.sb.solutions.api.authorization.service;

import com.sb.solutions.api.authorization.entity.ApprovalRoleHierarchy;
import com.sb.solutions.core.enums.ApprovalType;
import com.sb.solutions.core.service.BaseService;

import java.util.List;

public interface ApprovalRoleHierarchyService extends BaseService<ApprovalRoleHierarchy> {
    List<ApprovalRoleHierarchy> getForwardRolesForRole(Long roleId,
                                                       ApprovalType approvalType, Long typeId,
                                                       ApprovalType refType, Long refId);

    List<ApprovalRoleHierarchy> getForwardRolesForRoleWithType(Long roleId,
                                                               ApprovalType approvalType, Long refId);

    List<ApprovalRoleHierarchy> getBackwardRolesForRole(Long roleId,
                                                        ApprovalType approvalType, Long typeId,
                                                        ApprovalType refType, Long refId);

    List<ApprovalRoleHierarchy> getBackwardRolesForRoleWithType(Long roleId,
                                                                ApprovalType approvalType, Long refId);

    List<ApprovalRoleHierarchy> getRoles(ApprovalType approvalType, Long refId);
    List<ApprovalRoleHierarchy> getDefaultRoleHierarchies(ApprovalType approvalType, Long refId);

    boolean checkRoleContainInHierarchies(Long id,ApprovalType approvalType,Long refId);
}
