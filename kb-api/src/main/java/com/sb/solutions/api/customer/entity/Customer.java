package com.sb.solutions.api.customer.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.address.district.entity.District;
import com.sb.solutions.api.address.municipalityVdc.entity.MunicipalityVdc;
import com.sb.solutions.api.address.province.entity.Province;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.Status;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "customer")
public class Customer extends BaseEntity<Long> {

    private String title;
    private String customerName;
    private String customerId;
    @Temporal(TemporalType.DATE)
    private Date dob;
    private String accountNo;
    @ManyToOne
    private Province province;
    @ManyToOne
    private District district;
    @ManyToOne
    private MunicipalityVdc municipalities;
    private String street;
    private String wardNumber;
    private String contactNumber;
    private String occupation;
    private String incomeSource;
    private String email;
    private Date initialRelationDate;
    private String citizenshipNumber;
    private Date citizenshipIssuedDate;

    @OneToOne
    private District citizenshipIssuedDistrict;
    private Status status = Status.ACTIVE;
}
