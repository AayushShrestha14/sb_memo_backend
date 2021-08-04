package com.sb.solutions.api.creditmemo.service;

import com.sb.solutions.api.creditmemo.entity.CreditMemoType;
import com.sb.solutions.core.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @author Elvin Shrestha on 7/7/2020
 */
public interface CreditMemoTypeService extends BaseService<CreditMemoType> {

    List<CreditMemoType> findByMemoRoles();
    Map<String, Integer> statusCount();

}
