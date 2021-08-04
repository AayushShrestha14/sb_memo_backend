package com.sb.solutions.api.companyInfo.capital.entity;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Capital extends BaseEntity<Long> {

    private double authorizedCapital;

    private double paidUpCapital;

    private double issuedCapital;

    private double totalCapital;

    private double fixedCapital;

    private double workingCapital;

    private int numberOfShareholder;

}
