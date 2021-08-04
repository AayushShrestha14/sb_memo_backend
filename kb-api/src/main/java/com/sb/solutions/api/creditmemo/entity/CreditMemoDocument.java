package com.sb.solutions.api.creditmemo.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.document.entity.Document;
import com.sb.solutions.core.enitity.BaseEntity;

/**
 * @author Elvin Shrestha on 7/7/2020
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreditMemoDocument extends BaseEntity<Long> {

    @OneToOne
    private Document document;

    private String path;
}
