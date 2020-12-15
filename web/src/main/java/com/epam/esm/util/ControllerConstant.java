package com.epam.esm.util;

public enum ControllerConstant {

    DELETE_RELATION(Constant.DELETE_RELATION),
    UPDATE_RELATION(Constant.UPDATE_RELATION),

    ORDERS_RELATION(Constant.ORDERS_RELATION),
    ALL_USER_ORDERS(Constant.ALL_USER_ORDERS_RELATION),
    CERTIFICATES_RELATION(Constant.CERTIFICATES_RELATION),

    CERTIFICATE_NAME_EXAMPLE(Constant.CERTIFICATE_NAME_EXAMPLE),
    CERTIFICATE_DESCRIPTION_EXAMPLE(Constant.CERTIFICATE_DESCRIPTION_EXAMPLE),

    SORT_BY_NAME_ASC_RELATION(Constant.SORT_BY_NAME_ASC_RELATION),
    SORT_BY_NAME_DESC_RELATION(Constant.SORT_BY_NAME_DESC_RELATION),
    SORT_BY_DATE_ASC_RELATION(Constant.SORT_BY_DATE_ASC_RELATION),
    SORT_BY_DATE_DESC_RELATION(Constant.SORT_BY_DATE_DESC_RELATION),

    SEARCH_BY_NAME(Constant.SEARCH_BY_NAME),
    SEARCH_BY_DESCRIPTION(Constant.SEARCH_BY_DESCRIPTION),

    NEXT_PAGE(Constant.NEXT_PAGE),
    PREVIOUS_PAGE(Constant.PREVIOUS_PAGE),

    PAGES_NUMBER(Constant.PAGES_NUMBER),
    CURRENT_PAGE(Constant.CURRENT_PAGE),
    ELEMENTS_PER_PAGE(Constant.ELEMENTS_PER_PAGE),
    ELEMENTS_NUMBER(Constant.ELEMENTS_NUMBER),
    ALL_ENDPOINTS(Constant.ALL_ENDPOINTS);

    private String value;

    ControllerConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static class Constant {

        private static final String DELETE_RELATION = "delete";
        private static final String UPDATE_RELATION = "update";

        private static final String NEXT_PAGE = "next";
        private static final String PREVIOUS_PAGE = "prev";

        private static final String PAGES_NUMBER = "pages number";
        private static final String CURRENT_PAGE = "current page";
        private static final String ELEMENTS_PER_PAGE = "elements per page";
        private static final String ELEMENTS_NUMBER = "total number of elements";

        private static final String ORDERS_RELATION = "orders";
        private static final String ALL_USER_ORDERS_RELATION = "other orders of this user";
        private static final String CERTIFICATES_RELATION = "certificates";

        private static final String CERTIFICATE_NAME_EXAMPLE = "name";
        private static final String CERTIFICATE_DESCRIPTION_EXAMPLE = "description";

        private static final String SORT_BY_NAME_ASC_RELATION = "sort by name asc";
        private static final String SORT_BY_NAME_DESC_RELATION = "sort by name desc";
        private static final String SORT_BY_DATE_ASC_RELATION = "sort by creation date asc";
        private static final String SORT_BY_DATE_DESC_RELATION = "sort by creation date desc";

        private static final String SEARCH_BY_NAME = "search by name of certificate";
        private static final String SEARCH_BY_DESCRIPTION = "search by description of certificate";

        private static final String ALL_ENDPOINTS = "/**";
    }
}
