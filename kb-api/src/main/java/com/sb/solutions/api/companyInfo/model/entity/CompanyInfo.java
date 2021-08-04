package com.sb.solutions.api.companyInfo.model.entity;

import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import com.sb.solutions.api.companyInfo.capital.entity.Capital;
import com.sb.solutions.api.companyInfo.legalStatus.entity.LegalStatus;
import com.sb.solutions.api.companyInfo.managementTeam.entity.ManagementTeam;
import com.sb.solutions.api.companyInfo.proprietor.entity.Proprietor;
import com.sb.solutions.api.companyInfo.swot.entity.Swot;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.BusinessType;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CompanyInfo extends BaseEntity<Long> {

    @OneToOne(cascade = {
        CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private LegalStatus legalStatus;
    @OneToOne(cascade = {
        CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private Capital capital;
    @OneToOne(cascade = {
        CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private Swot swot;
    @OneToMany(fetch = FetchType.LAZY, cascade = {
        CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private Set<ManagementTeam> managementTeamList;
    @OneToMany(fetch = FetchType.LAZY, cascade = {
        CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private Set<Proprietor> proprietorsList;

    private String companyName;
    private String registrationNumber;
    private Date establishmentDate;
    private BusinessType businessType;
    private String panNumber;

    @PrePersist
    public void prePersist() {
        Date date = new Date();
        this.setLastModifiedAt(date);
        if (this.capital != null) {
            this.capital.setLastModifiedAt(date);
        }
        if (this.legalStatus != null) {
            this.legalStatus.setLastModifiedAt(date);
        }
        if (this.swot != null) {
            this.swot.setLastModifiedAt(date);
        }
        if (CollectionUtils.isNotEmpty(this.managementTeamList)) {
            for (ManagementTeam managementTeam : this.managementTeamList) {
                managementTeam.setLastModifiedAt(date);
            }
        }
        if (CollectionUtils.isNotEmpty(this.proprietorsList)) {
            for (Proprietor proprietor : this.proprietorsList) {
                proprietor.setLastModifiedAt(date);
            }
        }
    }
}
