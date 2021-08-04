package com.sb.solutions.api.accountPurpose.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.accountPurpose.entity.AccountPurpose;
import com.sb.solutions.api.accountPurpose.repository.AccountPurposeRepository;

@Service
public class AccountPurposeServiceImpl implements AccountPurposeService {

    private final AccountPurposeRepository accountPurposeRepository;

    public AccountPurposeServiceImpl(
        @Autowired AccountPurposeRepository accountPurposeRepository
    ) {
        this.accountPurposeRepository = accountPurposeRepository;
    }

    @Override
    public List<AccountPurpose> findAll() {
        return accountPurposeRepository.findAll();
    }

    @Override
    public AccountPurpose findOne(Long id) {
        return accountPurposeRepository.getOne(id);
    }

    @Override
    public AccountPurpose save(AccountPurpose accountPurpose) {
        return accountPurposeRepository.save(accountPurpose);
    }

    @Override
    public Page<AccountPurpose> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<AccountPurpose> saveAll(List<AccountPurpose> list) {
        return accountPurposeRepository.saveAll(list);
    }
}
