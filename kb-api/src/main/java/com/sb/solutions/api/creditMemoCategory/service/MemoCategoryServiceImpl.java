package com.sb.solutions.api.creditMemoCategory.service;

import com.sb.solutions.api.creditMemoCategory.entity.MemoCategory;
import com.sb.solutions.api.creditMemoCategory.repository.MemoCategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return memoCategoryRepository.findAll(pageable);
    }

    @Override
    public List<MemoCategory> saveAll(List<MemoCategory> list) {
        return memoCategoryRepository.saveAll(list);
    }
}
