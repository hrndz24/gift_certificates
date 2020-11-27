package com.epam.esm.utils;

import com.epam.esm.specification.SearchConditionSpecification;
import com.epam.esm.specification.impl.order.GetOrdersByUserId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OrderQueryGenerator {

    private List<SearchConditionSpecification> specifications;

    public List<SearchConditionSpecification> generateQuery(Map<String, String> params) {
        specifications = new ArrayList<>();
        appendQueryConditions(params);
        return specifications;
    }

    private void appendQueryConditions(Map<String, String> params) {
        params.keySet().forEach(key -> {
            if (ServiceConstant.USER_ID_PARAM.getValue().equals(key)) {
                specifications.add(new GetOrdersByUserId(Integer.parseInt(params.get(key))));
            }
        });
    }
}
