package com.sb.solutions.api.template.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sb.solutions.api.template.entity.Template;

public interface TemplateRepository extends JpaRepository<Template, Long>,
    JpaSpecificationExecutor<Template>
{
    Page<Template> findAll(Specification<Template> build, Pageable pageable);
}
