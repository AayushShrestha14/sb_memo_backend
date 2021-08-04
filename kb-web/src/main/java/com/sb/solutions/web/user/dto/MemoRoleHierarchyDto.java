package com.sb.solutions.web.user.dto;

import com.sb.solutions.api.rolePermissionRight.entity.MemoRole;
import com.sb.solutions.api.rolePermissionRight.entity.Role;
import com.sb.solutions.core.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MemoRoleHierarchyDto extends BaseDto<Long> {
    private MemoRole role;
    private Long roleOrder;
}
