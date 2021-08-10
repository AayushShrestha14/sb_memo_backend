package com.sb.solutions.api.creditmemo.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.sb.solutions.api.creditMemoCategory.entity.MemoCategory;
import com.sb.solutions.api.document.entity.Document;
import com.sb.solutions.api.rolePermissionRight.entity.MemoRole;
import com.sb.solutions.api.rolePermissionRight.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.Status;
import org.hibernate.annotations.Where;

import java.util.List;

/**
 * @author Elvin Shrestha on 7/7/2020
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreditMemoType extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    @NotNull
    private String name;

    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @ManyToMany
    @Where(clause = "status=1")
    private List<Document> documents;

    @ManyToMany
    @Where(clause = "status=1")
    private List<Role> roles;

    @ManyToMany
    @Where(clause = "status=1")
    private List<MemoCategory> category;
}
