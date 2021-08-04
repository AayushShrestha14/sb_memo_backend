package com.sb.solutions.api.rolePermissionRight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.rolePermissionRight.entity.RoleHierarchy;

/**
 * @author Rujan Maharjan on 5/13/2019
 */

@Repository
public interface RoleHierarchyRepository extends JpaRepository<RoleHierarchy, Long> {

    @Query("SELECT"
        + " new com.sb.solutions.api.rolePermissionRight.entity.RoleHierarchy("
        + "r.roleOrder,r.role.roleName,r.role.id)"
        + " FROM RoleHierarchy r   WHERE r.roleOrder < :id AND r.role.roleType <> 0 "
        + "ORDER BY r.roleOrder")
    List<RoleHierarchy> roleHierarchyByCurrentRoleForward(@Param("id") Long id);

    @Query("SELECT new com.sb.solutions.api.rolePermissionRight.entity.RoleHierarchy("
        + "r.roleOrder,r.role.roleName,r.role.id) FROM RoleHierarchy r"
        + "   WHERE r.roleOrder < :id AND r.role.roleType <> 0 ORDER BY r.roleOrder")
    List<RoleHierarchy> roleHierarchyByCurrentRoleBackward(@Param("id") Long id);

    @Query("SELECT r FROM RoleHierarchy r WHERE r.role.id=:id ")
    RoleHierarchy findByRole(@Param("id") Long id);

    @Query("SELECT r FROM RoleHierarchy r WHERE r.role.id <> 1 ORDER BY r.roleOrder ASC ")
    List<RoleHierarchy> findAll();

}
