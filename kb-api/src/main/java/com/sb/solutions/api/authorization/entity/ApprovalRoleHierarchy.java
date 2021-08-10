package com.sb.solutions.api.authorization.entity;

import com.sb.solutions.api.rolePermissionRight.entity.Role;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.ApprovalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
//@Table(name = "approval_role_hierarchy")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApprovalRoleHierarchy extends BaseEntity<Long> {
    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;
    private Long roleOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_type")
    private ApprovalType approvalType;

    private Long refId;
}
