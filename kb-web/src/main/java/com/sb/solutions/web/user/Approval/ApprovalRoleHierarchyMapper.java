package com.sb.solutions.web.user.Approval;

import com.sb.solutions.api.authorization.entity.ApprovalRoleHierarchy;
import com.sb.solutions.core.dto.BaseMapper;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = BaseMapper.SPRING_MODEL)
public abstract class ApprovalRoleHierarchyMapper extends
        BaseMapper<ApprovalRoleHierarchy, ApprovalRoleHierarchyDto> {

}
