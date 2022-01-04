package com.sb.solutions.api.creditMemoCategory.spec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.thymeleaf.util.MapUtils;

import com.sb.solutions.api.template.entity.Template;
import com.sb.solutions.api.template.repository.spec.TemplateSpec;

public class MemoCategorySpecBuilder {


    private final Map<String, String> params;


    public MemoCategorySpecBuilder(Map<String, String> params) {
        this.params = params;
    }

    public Specification<Template> build() {
        if (MapUtils.isEmpty(params)) {
            return null;
        }

        final List<String> properties = new ArrayList<>(params.keySet());

        final String firstProperty = properties.get(0);
        Specification<Template> spec = new TemplateSpec(properties.get(0),
            params.get(firstProperty));

        for (int i = 1; i < properties.size(); i++) {
            final String property = properties.get(i);
            spec = Specification.where(spec)
                .and(new TemplateSpec(property, params.get(property)));
        }


        return spec;
    }

}
