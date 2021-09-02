package com.sb.solutions.api.creditmemo.repository.spec;

import com.sb.solutions.api.creditmemo.entity.CreditMemo;
import com.sb.solutions.api.creditmemo.entity.CreditMemoType;
import org.springframework.data.jpa.domain.Specification;
import org.thymeleaf.util.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemoTypeSpecBuilder {

    private final Map<String, String> params;


    public MemoTypeSpecBuilder(Map<String, String> params) {
        this.params = params;
    }

    public Specification<CreditMemoType> build() {
        if (MapUtils.isEmpty(params)) {
            return null;
        }

        final List<String> properties = new ArrayList<>(params.keySet());

        final String firstProperty = properties.get(0);
        Specification<CreditMemoType> spec = new MemoTypeSpec(properties.get(0),
                params.get(firstProperty));

        for (int i = 1; i < properties.size(); i++) {
            final String property = properties.get(i);
            spec = Specification.where(spec)
                    .and(new MemoTypeSpec(property, params.get(property)));
        }


        return spec;
    }
}
