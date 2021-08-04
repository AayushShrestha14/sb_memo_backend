package com.sb.solutions.api.rolePermissionRight.service;

import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.rolePermissionRight.entity.MemoRole;
import com.sb.solutions.api.rolePermissionRight.repository.MemoRoleRepository;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MemoRoleServiceImpl implements MemoRoleService {

    private final MemoRoleRepository memoRoleRepository;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(MemoRoleService.class);

    public MemoRoleServiceImpl(@Autowired  MemoRoleRepository memoRoleRepository,
                               @Autowired  UserService userService) {
        this.memoRoleRepository = memoRoleRepository;
        this.userService = userService;
    }

    @Override
    public Map<Object, Object> MemoRoleStatusCount() {
        return memoRoleRepository.roleStatusCount();
    }

    @Override
    public List<Map<Object, Object>> activeMemoRole() {
        return memoRoleRepository.activeRole();
    }

    @Override
    public boolean isMaker() {
        return memoRoleRepository.chkByRoleType(RoleType.MAKER);
    }

    @Override
    public List<MemoRole> getApproval() {
        return memoRoleRepository.getByRoleTypeAndStatus(RoleType.APPROVAL,Status.ACTIVE);
    }

    @Override
    public List<MemoRole> findAll() {
        return memoRoleRepository.findAll();
    }

    @Override
    public MemoRole findOne(Long id) {
        return memoRoleRepository.findById(id).get();
    }

    @Override
    public MemoRole save(MemoRole memoRole) {

        memoRole.setRoleName(memoRole.getRoleName().toUpperCase());
        final MemoRole r = memoRoleRepository.save(memoRole);
//        if (memoRole.getId() != null && memoRole.getRoleType().equals(RoleType.COMMITTEE)) {
//            User u = new User();
//            u.setRole(r);
//            u.setName(u.getRole().getRoleName().concat("- default"));
//            u.setUsername(u.getRole().getRoleName().concat("- default"));
//            u.setPassword(u.getRole().getRoleName());
//            u.setBranch(new ArrayList<Branch>());
//            u.setEmail(u.getRole().getRoleName() + "@admin.com");
//            u.setStatus(Status.ACTIVE);
//            u.setIsDefaultCommittee(true);
//            userService.save(u);
//            logger.info("saving committee default user {}", u);
//
//        }


        return r;
    }

    @Override
    public Page<MemoRole> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<MemoRole> saveAll(List<MemoRole> list) {
        return memoRoleRepository.saveAll(list);
    }
}
