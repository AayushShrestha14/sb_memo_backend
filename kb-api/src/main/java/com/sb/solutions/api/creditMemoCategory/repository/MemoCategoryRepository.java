package com.sb.solutions.api.creditMemoCategory.repository;

import com.sb.solutions.api.creditMemoCategory.entity.MemoCategory;
import com.sb.solutions.api.template.entity.Template;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoCategoryRepository extends JpaRepository<MemoCategory, Long> {
    Page<MemoCategory> findAll(Specification<Template> build, Pageable pageable);
}
