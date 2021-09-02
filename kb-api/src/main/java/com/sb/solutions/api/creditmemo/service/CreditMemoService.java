package com.sb.solutions.api.creditmemo.service;

import java.util.List;
import java.util.Map;

import com.sb.solutions.api.creditmemo.entity.CreditMemo;
import com.sb.solutions.core.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Elvin Shrestha on 7/7/2020
 */
public interface CreditMemoService extends BaseService<CreditMemo> {
    CreditMemo action(CreditMemo creditMemo);
    Map<String, Integer> statusCount();
    Page<CreditMemo> findAllPageableForLoanAssociated(Object t, Pageable pageable);
    Page<CreditMemo> findAllMemoTypePageableWithFilter(Object t, Pageable pageable);
    List<CreditMemo> findByBranch();
}
