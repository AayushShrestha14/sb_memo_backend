package com.sb.solutions.api.creditmemo.repository.spec;

import com.sb.solutions.api.creditmemo.entity.CreditMemo;
import org.apache.commons.collections4.MapUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemoSpecBuilder {
    private final Map<String, String> params;

    public MemoSpecBuilder(Map<String, String> params) {
        this.params = params;
    }

    public Specification<CreditMemo> build() {
        if (MapUtils.isEmpty(params)) {
            return null;
        }

        final List<String> properties = new ArrayList<>(params.keySet());

        final String firstProperty = properties.get(0);

        Specification<CreditMemo> spec = new MemoSpec(properties.get(0),
                params.get(firstProperty));

        for (int i = 1; i < properties.size(); i++) {
            final String property = properties.get(i);
            spec = Specification.where(spec)
                    .and(new MemoSpec(property, params.get(property)));
        }

        return spec;

    }

}
