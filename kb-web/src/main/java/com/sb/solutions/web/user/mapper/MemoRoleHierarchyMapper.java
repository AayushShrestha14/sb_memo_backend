package com.sb.solutions.web.user.mapper;

import com.sb.solutions.api.rolePermissionRight.entity.MemoRoleHierarchy;
import com.sb.solutions.core.dto.BaseMapper;
import com.sb.solutions.web.user.dto.MemoRoleHierarchyDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = BaseMapper.SPRING_MODEL)
public abstract class MemoRoleHierarchyMapper extends BaseMapper<MemoRoleHierarchy, MemoRoleHierarchyDto> {
}
