package com.sb.solutions.api.creditmemo.repository;

import java.util.List;
import java.util.Map;

import com.sb.solutions.api.address.district.entity.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sb.solutions.api.creditmemo.entity.CreditMemo;

/**
 * @author Elvin Shrestha on 7/7/2020
 */
public interface CreditMemoRepository extends JpaRepository<CreditMemo, Long>,
    JpaSpecificationExecutor<CreditMemo> {

    @Query(value = "SELECT "
        + "(SELECT COUNT(cm.id) FROM credit_memo  cm "
        + " WHERE cm.document_status=0) Pending,"

        + "(SELECT COUNT(cm.id) FROM credit_memo cm WHERE cm.document_status=1 ) Approved,"

        + "(SELECT COUNT(cm.id) FROM credit_memo cm  WHERE cm.document_status=2) Rejected",nativeQuery = true)
    Map<String, Integer> statusCount();

}
