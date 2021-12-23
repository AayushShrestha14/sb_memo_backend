package com.sb.solutions.api.template.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.Status;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Template extends BaseEntity<Long> {

    private static final Logger logger = LoggerFactory.getLogger(Template.class);

    @NotNull
    private String name;

    @NotNull
    private String content;

    private Status status;
}
