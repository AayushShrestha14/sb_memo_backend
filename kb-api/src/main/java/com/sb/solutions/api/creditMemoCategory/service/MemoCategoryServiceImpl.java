package com.sb.solutions.api.creditMemoCategory.service;

import com.sb.solutions.api.creditMemoCategory.entity.MemoCategory;
import com.sb.solutions.api.creditMemoCategory.repository.MemoCategoryRepository;
import com.sb.solutions.api.creditMemoCategory.spec.MemoCategorySpecBuilder;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class MemoCategoryServiceImpl implements MemoCategoryService {

    private final MemoCategoryRepository memoCategoryRepository;

    public MemoCategoryServiceImpl(MemoCategoryRepository memoCategoryRepository) {
        this.memoCategoryRepository = memoCategoryRepository;
    }

    @Override
    public List<MemoCategory> findAll() {
        return memoCategoryRepository.findAll();
    }

    @Override
    public MemoCategory findOne(Long id) {
        return memoCategoryRepository.findById(id).get();
    }

    @Override
    public MemoCategory save(MemoCategory memoCategory) {
        return memoCategoryRepository.save(memoCategory);
    }

    @Override
    public Page<MemoCategory> findAllPageable(Object t, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> search = objectMapper.convertValue(t,Map.class);
        search.values().removeIf(Objects::isNull);
        MemoCategorySpecBuilder builder = new MemoCategorySpecBuilder(search);
        return memoCategoryRepository.findAll(builder.build(),pageable);
    }

    @Override
    public List<MemoCategory> saveAll(List<MemoCategory> list) {
        return memoCategoryRepository.saveAll(list);
    }
}
