package com.sb.solutions.api.companyInfo.swot.entity;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Swot extends BaseEntity<Long> {

    private String strength;

    private String weakness;

    private String opportunity;

    private String threats;

}
