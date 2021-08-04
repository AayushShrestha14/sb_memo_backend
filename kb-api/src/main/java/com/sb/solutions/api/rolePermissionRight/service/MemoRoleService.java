package com.sb.solutions.api.rolePermissionRight.service;

import com.sb.solutions.api.rolePermissionRight.entity.MemoRole;
import com.sb.solutions.core.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface MemoRoleService extends BaseService<MemoRole> {
    Map<Object, Object> MemoRoleStatusCount();

    List<Map<Object, Object>> activeMemoRole();

    boolean isMaker();

    List<MemoRole> getApproval();
}
