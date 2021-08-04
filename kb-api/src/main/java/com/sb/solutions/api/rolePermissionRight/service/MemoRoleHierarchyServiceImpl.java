package com.sb.solutions.api.rolePermissionRight.service;

import com.sb.solutions.api.rolePermissionRight.entity.MemoRoleHierarchy;
import com.sb.solutions.api.rolePermissionRight.repository.MemoRoleHierarchyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoRoleHierarchyServiceImpl implements MemoRoleHierarchyService {

    @Autowired
    private MemoRoleHierarchyRepository memoRoleHierarchyRepository;

    @Override
    public List<MemoRoleHierarchy> saveList(List<MemoRoleHierarchy> roleHierarchyList) {
        return memoRoleHierarchyRepository.saveAll(roleHierarchyList);
    }

    @Override
    public List<MemoRoleHierarchy> roleHierarchyByCurrentRoleForward(Long id) {
        MemoRoleHierarchy roleHierarchy = memoRoleHierarchyRepository.findByRole(id);
        return memoRoleHierarchyRepository.roleHierarchyByCurrentRoleForward(roleHierarchy.getRoleOrder());
    }

    @Override
    public List<MemoRoleHierarchy> roleHierarchyByCurrentRoleBackward(Long id) {
        MemoRoleHierarchy roleHierarchy = memoRoleHierarchyRepository.findByRole(id);
        return memoRoleHierarchyRepository.roleHierarchyByCurrentRoleBackward(roleHierarchy.getRoleOrder());

    }

    @Override
    public List<MemoRoleHierarchy> findAll() {
        return memoRoleHierarchyRepository.findAll();
    }

    @Override
    public MemoRoleHierarchy findOne(Long id) {
        return memoRoleHierarchyRepository.findById(id).get();
    }

    @Override
    public MemoRoleHierarchy save(MemoRoleHierarchy memoRoleHierarchy) {
        return memoRoleHierarchyRepository.save(memoRoleHierarchy);
    }

    @Override
    public Page<MemoRoleHierarchy> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<MemoRoleHierarchy> saveAll(List<MemoRoleHierarchy> list) {
        return memoRoleHierarchyRepository.saveAll(list);
    }
}
