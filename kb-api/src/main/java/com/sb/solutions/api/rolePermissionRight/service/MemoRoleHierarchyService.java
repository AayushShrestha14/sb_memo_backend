package com.sb.solutions.api.rolePermissionRight.service;

import com.sb.solutions.api.rolePermissionRight.entity.MemoRoleHierarchy;
import com.sb.solutions.core.service.BaseService;

import java.util.List;

public interface MemoRoleHierarchyService extends BaseService<MemoRoleHierarchy> {
    List<MemoRoleHierarchy> saveList(List<MemoRoleHierarchy> roleHierarchyList);

    List<MemoRoleHierarchy> roleHierarchyByCurrentRoleForward(Long id);

    List<MemoRoleHierarchy> roleHierarchyByCurrentRoleBackward(Long id);
}
