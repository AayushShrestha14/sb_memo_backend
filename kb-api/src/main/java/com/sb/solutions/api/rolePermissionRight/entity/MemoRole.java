package com.sb.solutions.api.rolePermissionRight.entity;

import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.RoleAccess;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class MemoRole extends BaseEntity<Long> implements Serializable {
    @Transient
    private Long id;
    @Column(unique = true, nullable = false)
    private String roleName;

    private Status status = Status.ACTIVE;

    @Transient
    private String createdByName;

    @Transient
    private String modifiedByName;


    private RoleType roleType = RoleType.APPROVAL;

    @NotNull
    private RoleAccess roleAccess = RoleAccess.OWN;

    @Transient
    private int version;
}
