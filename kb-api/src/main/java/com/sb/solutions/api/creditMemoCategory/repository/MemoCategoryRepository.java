package com.sb.solutions.api.creditMemoCategory.repository;

import com.sb.solutions.api.creditMemoCategory.entity.MemoCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoCategoryRepository extends JpaRepository<MemoCategory, Long> {
}
