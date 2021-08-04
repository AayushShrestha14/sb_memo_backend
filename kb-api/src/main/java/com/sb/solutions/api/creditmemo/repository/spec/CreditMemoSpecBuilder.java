package com.sb.solutions.api.creditmemo.repository.spec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.creditmemo.entity.CreditMemo;

/**
 * @author Elvin Shrestha on 7/7/2020
 */
public class CreditMemoSpecBuilder {

    private final Map<String, String> params;

    public CreditMemoSpecBuilder(Map<String, String> params) {
        this.params = params;
    }

    public Specification<CreditMemo> build() {
        if (MapUtils.isEmpty(params)) {
            return null;
        }

        final List<String> properties = new ArrayList<>(params.keySet());

        final String firstProperty = properties.get(0);

        Specification<CreditMemo> spec = new CreditMemoSpec(properties.get(0),
            params.get(firstProperty));

        for (int i = 1; i < properties.size(); i++) {
            final String property = properties.get(i);
            spec = Specification.where(spec)
                .and(new CreditMemoSpec(property, params.get(property)));
        }

        return spec;

    }
}
