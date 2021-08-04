package com.sb.solutions.api.rolePermissionRight.service;

import java.util.List;

import com.sb.solutions.api.rolePermissionRight.entity.RolePermissionRights;
import com.sb.solutions.core.service.BaseService;

/**
 * @author Rujan Maharjan on 3/28/2019
 */
public interface RolePermissionRightService extends BaseService<RolePermissionRights> {

    List<RolePermissionRights> getByRoleId(Long id);

    void saveList(List<RolePermissionRights> rolePermissionRightsList);


}
