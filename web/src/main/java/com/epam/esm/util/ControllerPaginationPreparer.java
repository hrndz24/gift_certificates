package com.epam.esm.util;

import com.epam.esm.utils.ServiceConstant;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class ControllerPaginationPreparer {

    public List<Link> preparePaginationLinks(Object invocationValue, Map<String, String> params, long entityCount) {
        int currentPage = getPageNumber(params);
        long pageCount = calculatePageCount(params, entityCount);
        List<Link> links = new ArrayList<>();
        if (currentPage != pageCount) {
            params.replace(ServiceConstant.PAGE_PARAM.getValue(), String.valueOf(currentPage + 1));
            links.add(linkTo(invocationValue).withRel(ControllerConstant.NEXT_PAGE.getValue()));
        }
        if (currentPage != 1) {
            params.replace(ServiceConstant.PAGE_PARAM.getValue(), String.valueOf(currentPage - 1));
            links.add(linkTo(invocationValue).withRel(ControllerConstant.PREVIOUS_PAGE.getValue()));
        }
        return links;
    }

    public Map<String, Long> preparePageInfo(Map<String, String> params, long entityCount) {
        int currentPage = getPageNumber(params);
        long pageCount = calculatePageCount(params, entityCount);
        Map<String, Long> page = new HashMap<>();
        page.put(ControllerConstant.PAGES_NUMBER.getValue(), pageCount);
        page.put(ControllerConstant.CURRENT_PAGE.getValue(), (long) currentPage);
        page.put(ControllerConstant.ELEMENTS_PER_PAGE.getValue(), Long.parseLong(params.get(ServiceConstant.SIZE_PARAM.getValue())));
        page.put(ControllerConstant.ELEMENTS_NUMBER.getValue(), entityCount);
        return page;
    }

    private int getPageNumber(Map<String, String> params) {
        return Integer.parseInt(params.get(ServiceConstant.PAGE_PARAM.getValue()));
    }

    private long calculatePageCount(Map<String, String> params, long entityCount) {
        return (long) Math.ceil(entityCount / Double.parseDouble(params.get(ServiceConstant.SIZE_PARAM.getValue())));
    }
}
