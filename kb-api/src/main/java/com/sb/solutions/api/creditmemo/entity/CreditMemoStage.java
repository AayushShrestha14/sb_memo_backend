package com.sb.solutions.api.creditmemo.entity;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.sb.solutions.api.stage.entity.Stage;

/**
 * @author Elvin Shrestha on 7/7/2020
 */
@Entity
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreditMemoStage extends Stage {

}
