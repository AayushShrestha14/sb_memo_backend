package com.sb.solutions.api.rolePermissionRight.entity;

import com.sb.solutions.core.enitity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemoRoleHierarchy extends BaseEntity<Long> {

    @OneToOne
    @JoinColumn(name = "role_id")
    private MemoRole role;
    private Long roleOrder;

    @Transient
    private String roleName;

    @Transient
    private Long roleId;

    @Transient
    private Long id;

    @Transient
    private Long userId;

    public MemoRoleHierarchy(Long roleOrder, String roleName, Long id) {
        this.roleOrder = roleOrder;
        this.roleName = roleName;
        this.id = id;

    }
}
