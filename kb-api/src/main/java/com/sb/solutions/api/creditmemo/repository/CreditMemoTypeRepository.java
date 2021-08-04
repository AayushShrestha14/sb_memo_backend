package com.sb.solutions.api.creditmemo.repository;

import com.sb.solutions.api.rolePermissionRight.entity.MemoRole;
import com.sb.solutions.api.rolePermissionRight.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.solutions.api.creditmemo.entity.CreditMemoType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Elvin Shrestha on 7/7/2020
 */
public interface CreditMemoTypeRepository extends JpaRepository<CreditMemoType, Long> {
    @Query(value = "select b from CreditMemoType b where b.name like concat(:name,'%')")
    Page<CreditMemoType> creditMemoTypeFilter(@Param("name") String name, Pageable pageable);

    List<CreditMemoType> findByRoles(Role role);



}
