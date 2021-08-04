package com.sb.solutions.api.rolePermissionRight.repository;

import com.sb.solutions.api.rolePermissionRight.dto.RoleDto;
import com.sb.solutions.api.rolePermissionRight.entity.MemoRole;
import com.sb.solutions.api.rolePermissionRight.entity.Role;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MemoRoleRepository extends JpaRepository<MemoRole, Long> {
    @Query(value = "select "
            + "  (select  count(id) from memo_role where status=1) active, "
            + " (select  count(id) from memo_role where status=0) inactive, "
            + " (select  count(id) from memo_role) roles ", nativeQuery = true)
    Map<Object, Object> roleStatusCount();

    @Query(value = "Select distinct p.id as id,p.role_name as roleName from memo_role p "
            + "where p.status=1 and p.id <> 1 ", nativeQuery = true)
    List<Map<Object, Object>> activeRole();

    @Query("select"
            + " new com.sb.solutions.api.rolePermissionRight.entity.MemoRole(r.id,r.roleName,r.status,"
            + " (SELECT u.username from User u where r.createdBy=u.id),"
            + " (SELECT u.username from User u where r.modifiedBy=u.id),"
            + " r.roleType,r.roleAccess,r.version) from MemoRole r")
    List<MemoRole> findAll();

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM MemoRole c"
            + " WHERE c.roleType = :roleType")
    boolean chkByRoleType(@Param("roleType") RoleType roleType);

    List<MemoRole> getByRoleTypeAndStatus(RoleType roleType, Status status);

//    @Query("select new com.sb.solutions.api.rolePermissionRight.dto.RoleDto(r.id,r.roleName)  from Role r")
//    List<RoleDto> getRoleDto();
}
