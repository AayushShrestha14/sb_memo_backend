package com.sb.solutions.web.user.dto;

import lombok.Data;

import com.sb.solutions.core.dto.BaseDto;

/**
 * @author Rujan Maharjan on 6/12/2019
 */

@Data
public class RoleDto extends BaseDto<Long> {

    private String roleName;
}
