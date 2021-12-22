package com.sb.solutions.api.template.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.template.entity.Template;
import com.sb.solutions.api.template.repository.TemplateRepository;

@Service
public class TemplateServiceImpl implements TemplateService{
    private final TemplateRepository repository;

    public TemplateServiceImpl(
        TemplateRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Template> findAll() {
        return repository.findAll();
    }

    @Override
    public Template findOne(Long id) {
        return repository.getOne(id);
    }

    @Override
    public Template save(Template template) {
        return repository.save(template);
    }

    @Override
    public Page<Template> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<Template> saveAll(List<Template> list) {
        return null;
    }
}
