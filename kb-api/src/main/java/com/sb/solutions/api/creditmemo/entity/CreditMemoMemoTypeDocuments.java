package com.sb.solutions.api.creditmemo.entity;

import com.sb.solutions.api.document.entity.Document;
import com.sb.solutions.core.enitity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
/**
 * @author Himal Rai on 6/10/2021
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreditMemoMemoTypeDocuments extends BaseEntity<Long> {
    @OneToOne
    private Document document;

    private String path;
}
