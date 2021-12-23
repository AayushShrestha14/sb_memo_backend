package com.sb.solutions.api.template.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.template.entity.Template;
import com.sb.solutions.api.template.repository.TemplateRepository;
import com.sb.solutions.api.template.repository.spec.TemplateSpecBuilder;
import com.sb.solutions.core.enums.Status;

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

        if (template.getId() == null) {
            template.setStatus(Status.ACTIVE);
        }
        return repository.save(template);
    }

    @Override
    public Page<Template> findAllPageable(Object t, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> search = objectMapper.convertValue(t,Map.class);
        search.values().removeIf(Objects::isNull);
        TemplateSpecBuilder builder = new TemplateSpecBuilder(search);
        return repository.findAll(builder.build(), pageable);
    }

    @Override
    public List<Template> saveAll(List<Template> list) {
        return null;
    }
}
