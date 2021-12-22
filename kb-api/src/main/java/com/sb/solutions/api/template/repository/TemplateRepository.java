package com.sb.solutions.api.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.sb.solutions.api.template.entity.Template;

public interface TemplateRepository extends JpaRepository<Template, Long>,
    JpaSpecificationExecutor<Template>
{

}
