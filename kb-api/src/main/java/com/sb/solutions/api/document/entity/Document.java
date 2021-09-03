package com.sb.solutions.api.document.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.Status;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Document extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    private String displayName;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String url;

    private Status status;
}
