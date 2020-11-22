package com.epam.esm.util;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class ControllerPaginationPreparer {

    public List<Link> prepareLinks(Object invocationValue, Map<String, String> params, int currentPage, long count) {
        long pageCount = (long) Math.ceil(count / Double.parseDouble(params.get("size")));
        List<Link> links = new ArrayList<>();
        if (currentPage != pageCount) {
            params.replace("page", String.valueOf(currentPage + 1));
            links.add(linkTo(invocationValue).withRel("next"));
        }
        if (currentPage != 1) {
            params.replace("page", String.valueOf(currentPage - 1));
            links.add(linkTo(invocationValue).withRel("prev"));
        }
        return links;
    }

    public Map<String, Long> preparePageInfo(Map<String, String> params, int currentPage, long tagsCount) {
        long pageCount = (long) Math.ceil(tagsCount / Double.parseDouble(params.get("size")));
        Map<String, Long> page = new HashMap<>();
        page.put("pages number", pageCount);
        page.put("current page", (long) currentPage);
        page.put("elements per page", Long.parseLong(params.get("size")));
        page.put("elements number", tagsCount);
        return page;
    }
}
