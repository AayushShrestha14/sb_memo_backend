package com.sb.solutions.api.rolePermissionRight.repository;

import com.sb.solutions.api.rolePermissionRight.entity.MemoRoleHierarchy;
import com.sb.solutions.api.rolePermissionRight.entity.MemoRoleHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRoleHierarchyRepository extends JpaRepository<MemoRoleHierarchy, Long> {

    @Query("SELECT"
            + " new com.sb.solutions.api.rolePermissionRight.entity.MemoRoleHierarchy("
            + "r.roleOrder,r.role.roleName,r.role.id)"
            + " FROM MemoRoleHierarchy r   WHERE r.roleOrder < :id AND r.role.roleType <> 0 "
            + "ORDER BY r.roleOrder")
    List<MemoRoleHierarchy> roleHierarchyByCurrentRoleForward(@Param("id") Long id);

    @Query("SELECT new com.sb.solutions.api.rolePermissionRight.entity.MemoRoleHierarchy("
            + "r.roleOrder,r.role.roleName,r.role.id) FROM MemoRoleHierarchy r"
            + "   WHERE r.roleOrder < :id AND r.role.roleType <> 0 ORDER BY r.roleOrder")
    List<MemoRoleHierarchy> roleHierarchyByCurrentRoleBackward(@Param("id") Long id);

    @Query("SELECT r FROM MemoRoleHierarchy r WHERE r.role.id=:id ")
    MemoRoleHierarchy findByRole(@Param("id") Long id);

    @Query("SELECT r FROM MemoRoleHierarchy r WHERE r.role.id <> 1 ORDER BY r.roleOrder ASC ")
    List<MemoRoleHierarchy> findAll();
}
