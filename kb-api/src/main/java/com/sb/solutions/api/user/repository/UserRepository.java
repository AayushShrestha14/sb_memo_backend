package com.sb.solutions.api.user.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sb.solutions.api.rolePermissionRight.entity.Role;
import com.sb.solutions.api.user.PieChartDto;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.core.enums.RoleAccess;
import com.sb.solutions.core.enums.Status;

/**
 * @author Rujan Maharjan on 12/31/2018
 */
public interface UserRepository extends JpaRepository<User, Long>,
    JpaSpecificationExecutor<User> {

    @Query(value = "select b FROM User b WHERE b.name like concat(:name,'%')")
    Page<User> userFilter(@Param("name") String name, Pageable pageable);

    User getUsersByUsername(String username);

    User getUsersByUsernameAndStatus(String username, Status status);

    @Query(value = "SELECT * FROM users u JOIN users_branch ub ON ub.user_id=u.id"
        + " WHERE u.role_id=:role AND u.status=:status and ub.branch_id IN (:branch)", nativeQuery = true)
    List<User> findByRoleIdAndBranch(@Param("role") Long role, @Param("branch") List<Long> branch ,@Param("status") int status);

    List<User> findByRoleRoleAccessAndRoleNotAndRoleIdAndStatus(RoleAccess roleAccess, Role role, Long id , Status status);

    List<User> findByRoleId(Long roleId);

    @Query("SELECT u FROM User u where u.role is not null and u.status = :status")
    List<User> findUserNotDisMissAndActive(@Param("status") Status status);


    Page<User> findByRoleIn(Collection<Role> roles, Pageable pageable);

    @Query(value = "select "
        + "  (select  count(id) FROM users WHERE status=1) active,"
        + " (select  count(id) FROM users WHERE status=0) inactive,"
        + " (select  count(id) FROM users) users\n", nativeQuery = true)
    Map<Object, Object> userStatusCount();

    @Query(value = "select b FROM User b WHERE b.name like concat(:name,'%')")
    List<User> userCsvFilter(@Param("name") String name);

    List<User> findByRoleIdAndBranchId(Long roleId, Long branchId);

    @Query(value =
        "  SELECT a.type FROM role r "
            + " LEFT JOIN role_permission_rights rpr ON rpr.role_id = r.id"
            + " LEFT JOIN role_permission_rights_api_rights rprar"
            + " ON rprar.role_permission_rights_id=rpr.id"
            + " LEFT JOIN url_api a ON rprar.api_rights_id = a.id "
            + " WHERE "
            + " r.id = :id\n"
            + " AND a.type is not null;", nativeQuery = true)
    List<Object> userApiAuthorities(@Param("id") Long id);

    @Query("SELECT NEW com.sb.solutions.api.user.PieChartDto(b.name, COUNT(u)) FROM User u JOIN"
        + " u.branch b WHERE u.role.roleAccess = com.sb.solutions.core.enums.RoleAccess.OWN "
        + "GROUP BY b.id,b.name")
    List<PieChartDto> getStatisticsBasedOnBranch();

    @Query("SELECT NEW com.sb.solutions.api.user.PieChartDto(r.roleName, COUNT(u)) FROM User "
        + " u join u.role r GROUP BY r.id,r.roleName")
    List<PieChartDto> getStatisticsBasedOnRoles();

    List<User>  findByRoleIdAndIsDefaultCommitteeAndStatus(Long id, Boolean isCommittee , Status status);

}

