package com.epam.esm.utils;

public enum ServiceConstant {

    ORDER_BY_PARAM(Constant.ORDER_BY_PARAM),

    TAG_NAME_PARAM(Constant.TAG_NAME_PARAM),

    CERTIFICATE_NAME_PARAM(Constant.CERTIFICATE_NAME_PARAM),

    CERTIFICATE_DESCRIPTION_PARAM(Constant.CERTIFICATE_DESCRIPTION_PARAM),

    USER_ID_PARAM(Constant.USER_ID_PARAM),

    PAGE_PARAM(Constant.PAGE_PARAM), SIZE_PARAM(Constant.SIZE_PARAM),

    ID_FIELD(Constant.ID_FIELD), NAME_FIELD(Constant.NAME_FIELD),

    DESCRIPTION_FIELD(Constant.DESCRIPTION_FIELD),

    TAGS_FIELD(Constant.TAGS_FIELD), CREATED_DATE_FIELD(Constant.CREATED_DATE_FIELD),

    CERTIFICATES_ID_FIELD(Constant.CERTIFICATES_ID_FIELD),

    DURATION_FIELD(Constant.DURATION_FIELD), PRICE_FIELD(Constant.PRICE_FIELD),

    SORT_BY_NAME_ASC(Constant.SORT_BY_NAME_ASC),

    SORT_BY_NAME_DESC(Constant.SORT_BY_NAME_DESC),

    SORT_BY_DATE_ASC(Constant.SORT_BY_DATE_ASC),

    SORT_BY_DATE_DESC(Constant.SORT_BY_DATE_DESC),

    TAGS_TO_SEARCH_BY_SEPARATOR(Constant.TAGS_TO_SEARCH_BY_SEPARATOR);

    private final String value;

    ServiceConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static class Constant {

        private static final String ORDER_BY_PARAM = "orderBy";
        private static final String TAG_NAME_PARAM = "tagName";
        private static final String CERTIFICATE_NAME_PARAM = "certificateName";
        private static final String CERTIFICATE_DESCRIPTION_PARAM = "certificateDescription";

        private static final String USER_ID_PARAM = "userId";

        private static final String PAGE_PARAM = "page";
        private static final String SIZE_PARAM = "size";

        private static final String ID_FIELD = "id";
        private static final String NAME_FIELD = "name";
        private static final String DESCRIPTION_FIELD = "description";
        private static final String TAGS_FIELD = "tags";
        private static final String CREATED_DATE_FIELD = "createDate";
        private static final String DURATION_FIELD = "duration";
        private static final String PRICE_FIELD = "price";
        private static final String CERTIFICATES_ID_FIELD = "certificatesId";

        private static final String SORT_BY_NAME_ASC = "name";
        private static final String SORT_BY_NAME_DESC = "-name";
        private static final String SORT_BY_DATE_ASC = "date";
        private static final String SORT_BY_DATE_DESC = "-date";

        private static final String TAGS_TO_SEARCH_BY_SEPARATOR = ", ";
    }
}
