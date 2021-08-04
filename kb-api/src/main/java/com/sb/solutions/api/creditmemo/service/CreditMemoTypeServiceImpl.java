package com.sb.solutions.api.creditmemo.service;

import java.util.List;
import java.util.Map;

import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.dto.SearchDto;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.creditmemo.entity.CreditMemoType;
import com.sb.solutions.api.creditmemo.repository.CreditMemoTypeRepository;

/**
 * @author Elvin Shrestha on 7/7/2020
 */
@Service
public class CreditMemoTypeServiceImpl implements CreditMemoTypeService {

    private final CreditMemoTypeRepository repository;
    private  final UserService userService;

    public CreditMemoTypeServiceImpl(
            CreditMemoTypeRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public List<CreditMemoType> findAll() {
        return repository.findAll();
    }

    @Override
    public CreditMemoType findOne(Long id) {
        return repository.getOne(id);
    }

    @Override
    public CreditMemoType save(CreditMemoType creditMemoType) {
        return repository.save(creditMemoType);
    }

    @Override
    public Page<CreditMemoType> findAllPageable(Object t, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchDto s = objectMapper.convertValue(t, SearchDto.class);

        return repository.creditMemoTypeFilter(s.getName() == null ? "" : s.getName(), pageable);
    }

    @Override
    public List<CreditMemoType> saveAll(List<CreditMemoType> list) {
        return repository.saveAll(list);
    }

    @Override
    public List<CreditMemoType> findByMemoRoles() {
        User user = userService.getAuthenticated();
        if(user.getRole() != null) {
            return repository.findByRoles(user.getRole());
        }
        return null;
    }

    @Override
    public Map<String, Integer> statusCount() {
        return null;
    }
}
