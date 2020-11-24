package com.epam.esm.validation;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.exception.ValidatorException;
import com.epam.esm.utils.ServiceConstant;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Component
public class Validator {

    private Set<String> certificateParameterNames;
    private Set<String> orderParameterNames;
    private Set<String> tagParameterNames;
    private Set<String> userParameterNames;
    private Set<String> orderByValues;
    private Set<String> certificateFieldNames;
    private Set<String> tagFieldNames;
    private Set<String> paginationParameters;
    private static final int DEFAULT_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 1;

    private static final String NAME_FIELD = "name";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String TAGS_FIELD = "tags";
    private static final String DURATION_FIELD = "duration";
    private static final String PRICE_FIELD = "price";

    public Validator() {
        this.certificateParameterNames = new HashSet<>();
        this.orderParameterNames = new HashSet<>();
        this.tagParameterNames = new HashSet<>();
        this.userParameterNames = new HashSet<>();
        this.orderByValues = new HashSet<>();
        this.certificateFieldNames = new HashSet<>();
        this.tagFieldNames = new HashSet<>();
        this.paginationParameters = new HashSet<>();
        fillInParameterNames();
    }

    private void fillInParameterNames() {
        paginationParameters.add(ServiceConstant.PAGE_PARAM.getValue());
        paginationParameters.add(ServiceConstant.SIZE_PARAM.getValue());
        certificateParameterNames.add(ServiceConstant.CERTIFICATE_NAME_PARAM.getValue());
        certificateParameterNames.add(ServiceConstant.CERTIFICATE_DESCRIPTION_PARAM.getValue());
        certificateParameterNames.add(ServiceConstant.TAG_NAME_PARAM.getValue());
        certificateParameterNames.add(ServiceConstant.ORDER_BY_PARAM.getValue());
        certificateParameterNames.addAll(paginationParameters);
        orderParameterNames.add(ServiceConstant.USER_ID_PARAM.getValue());
        orderParameterNames.addAll(paginationParameters);
        tagParameterNames.addAll(paginationParameters);
        userParameterNames.addAll(paginationParameters);
        orderByValues.add(ServiceConstant.SORT_BY_NAME_ASC.getValue());
        orderByValues.add(ServiceConstant.SORT_BY_NAME_DESC.getValue());
        orderByValues.add(ServiceConstant.SORT_BY_DATE_ASC.getValue());
        orderByValues.add(ServiceConstant.SORT_BY_DATE_DESC.getValue());
        certificateFieldNames.add(ServiceConstant.NAME_FIELD.getValue());
        certificateFieldNames.add(ServiceConstant.DESCRIPTION_FIELD.getValue());
        certificateFieldNames.add(ServiceConstant.PRICE_FIELD.getValue());
        certificateFieldNames.add(ServiceConstant.DURATION_FIELD.getValue());
        certificateFieldNames.add(ServiceConstant.TAGS_FIELD.getValue());
        tagFieldNames.add(ServiceConstant.ID_FIELD.getValue());
        tagFieldNames.add(ServiceConstant.NAME_FIELD.getValue());
    }

    public void validateCertificate(GiftCertificateDTO giftCertificateDTO) {
        validateNonNull(giftCertificateDTO, GiftCertificateDTO.class.getName());
        validateStringField(giftCertificateDTO.getName(), "certificate name");
        validateStringField(giftCertificateDTO.getDescription(), "description");
        validatePrice(giftCertificateDTO.getPrice());
        validateDuration(giftCertificateDTO.getDuration());
    }

    public void validateTag(TagDTO tagDTO) {
        validateNonNull(tagDTO, TagDTO.class.getName());
        validateStringField(tagDTO.getName(), "tag name");
    }

    public void validateOrder(OrderDTO orderDTO) {
        validateNonNull(orderDTO, OrderDTO.class.getName());
        validateIdIsPositive(orderDTO.getUserId());
        checkOrderHasCertificates(orderDTO);
    }

    public void validateCertificateForOrdering(GiftCertificateDTO certificate) {
        validateNonNull(certificate, GiftCertificateDTO.class.getName());
        validateIdIsPositive(certificate.getId());
    }

    public void validateIdIsPositive(int id) {
        if (id <= 0) {
            throw new ValidatorException(
                    ServiceExceptionCode.SHOULD_BE_POSITIVE.getErrorCode(), "id = " + id);
        }
    }

    private void validateNonNull(Object object, String className) {
        if (object == null) {
            throw new ValidatorException(ServiceExceptionCode.CANNOT_BE_NULL.getErrorCode(), className);
        }
    }

    private void validateStringField(String string, String field) {
        if (!StringUtils.hasText(string)) {
            throw new ValidatorException(ServiceExceptionCode.CANNOT_BE_EMPTY.getErrorCode(), field);
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new ValidatorException(ServiceExceptionCode.CANNOT_BE_NULL.getErrorCode(), "price");
        }
        if (price.doubleValue() < 0) {
            throw new ValidatorException(ServiceExceptionCode.SHOULD_BE_POSITIVE.getErrorCode(), "price = " + price);
        }
        if (price.doubleValue() > 999999999.99) {
            throw new ValidatorException(ServiceExceptionCode.PRICE_TOO_HIGH.getErrorCode(), "price = " + price);
        }
    }

    private void validateDuration(int duration) {
        if (duration <= 0) {
            throw new ValidatorException(
                    ServiceExceptionCode.SHOULD_BE_POSITIVE.getErrorCode(), "duration = " + duration);
        }
        if (duration > 366) {
            throw new ValidatorException(
                    ServiceExceptionCode.DURATION_CANNOT_BE_MORE_THAN_YEAR.getErrorCode(), "duration = " + duration);
        }
    }

    public void validateCertificateParams(Map<String, String> params) {
        validateNonNull(params, params.getClass().getName());
        trimAndLowerCaseValues(params);
        checkParamsHavePaginationParameters(params);
        checkCertificateParametersExist(params);
        checkParamsHaveOrderBy(params);
        checkParamsHaveTagName(params);
    }

    private void checkParamsHaveTagName(Map<String, String> params) {
        if (params.containsKey(ServiceConstant.TAG_NAME_PARAM.getValue())) {
            params.replace(ServiceConstant.TAG_NAME_PARAM.getValue(),
                    validateTagNames(params.get(ServiceConstant.TAG_NAME_PARAM.getValue())));
        }
    }

    private String validateTagNames(String tagNamesAsString) {
        List<String> validatedTags = new ArrayList<>();
        String[] tagNames = tagNamesAsString.split(",");
        for (String tagName : tagNames) {
            validateStringField(tagName, "tag name");
            validatedTags.add(tagName.trim().toLowerCase());
        }
        return String.join(ServiceConstant.TAGS_TO_SEARCH_BY_SEPARATOR.getValue(), validatedTags.toArray(new String[0]));
    }

    public void validateOrderParams(Map<String, String> params) {
        validateNonNull(params, params.getClass().getName());
        trimAndLowerCaseValues(params);
        checkParamsHavePaginationParameters(params);
        checkOrderParametersExist(params);
    }

    public void validateTagParams(Map<String, String> params) {
        validateNonNull(params, params.getClass().getName());
        trimAndLowerCaseValues(params);
        checkParamsHavePaginationParameters(params);
        checkTagParameterExist(params);
    }

    private void checkTagParameterExist(Map<String, String> params) {
        params.forEach((key, value) -> {
            if (!tagParameterNames.contains(key)) {
                throw new ValidatorException(ServiceExceptionCode.NON_EXISTING_PARAM_NAME.getErrorCode(), key);
            }
        });
    }

    public void validateUserParams(Map<String, String> params) {
        validateNonNull(params, params.getClass().getName());
        trimAndLowerCaseValues(params);
        checkParamsHavePaginationParameters(params);
        checkUserParameterExist(params);
    }

    private void checkUserParameterExist(Map<String, String> params) {
        params.forEach((key, value) -> {
            if (!userParameterNames.contains(key)) {
                throw new ValidatorException(ServiceExceptionCode.NON_EXISTING_PARAM_NAME.getErrorCode(), key);
            }
        });
    }

    public void validateCertificateUpdateField(Map<String, Object> field) {
        validateNonNull(field, field.getClass().getName());
        checkThereIsOneUpdateField(field);
        checkCertificateFieldsExist(field);
        checkFieldValuesNonNull(field);
        validateFieldValue(field);
    }

    private void validateFieldValue(Map<String, Object> field) {
        field.entrySet().forEach(entry -> {
            switch (entry.getKey()) {
                case NAME_FIELD:
                    validateCertificateUpdateFieldMatchesDataType(String.class, entry);
                    validateStringField((String) entry.getValue(), "certificate name");
                    break;
                case DESCRIPTION_FIELD:
                    validateCertificateUpdateFieldMatchesDataType(String.class, entry);
                    validateStringField((String) entry.getValue(), "certificate description");
                    break;
                case PRICE_FIELD:
                    validateCertificateUpdateFieldMatchesDataType(Double.class, entry);
                    validatePrice(BigDecimal.valueOf((Double) entry.getValue()));
                    break;
                case DURATION_FIELD:
                    validateCertificateUpdateFieldMatchesDataType(Integer.class, entry);
                    validateDuration((Integer) entry.getValue());
                    break;
                case TAGS_FIELD:
                    validateCertificateUpdateFieldMatchesDataType(ArrayList.class, entry);
                    validateTagsField(entry.getValue());
                    break;
            }
        });
    }

    public void validateCertificateUpdateFieldMatchesDataType(Class<?> requested, Map.Entry<String, Object> field) {
        if (requested != field.getValue().getClass()) {
            throw new ValidatorException(
                    ServiceExceptionCode.DATA_TYPE_DOES_NOT_MATCH_REQUIRED.getErrorCode(), field.getKey());
        }
    }

    private void checkThereIsOneUpdateField(Map<String, Object> fields) {
        if (fields.size() != 1) {
            throw new ValidatorException
                    (ServiceExceptionCode.ONLY_ONE_FIELD_CAN_BE_UPDATED.getErrorCode());
        }
    }

    private void checkCertificateFieldsExist(Map<String, Object> fields) {
        fields.keySet().forEach((fieldName) -> {
            if (!certificateFieldNames.contains(fieldName)) {
                throw new ValidatorException(
                        ServiceExceptionCode.NON_EXISTING_CERTIFICATE_FIELD_NAME.getErrorCode(), fieldName);
            }
        });
    }

    private void checkOrderHasCertificates(OrderDTO order) {
        Set<GiftCertificateDTO> certificates = order.getCertificates();
        if (certificates == null || certificates.isEmpty()) {
            throw new ValidatorException(
                    ServiceExceptionCode.ORDER_SHOULD_HAVE_CERTIFICATES.getErrorCode());
        }
    }

    private void checkFieldValuesNonNull(Map<String, Object> fields) {
        fields.forEach((key, value) -> validateNonNull(value, key));
    }

    @SuppressWarnings("unchecked cast")
    private void validateTagsField(Object tagsValue) {
        List<?> tagList = (ArrayList<?>) tagsValue;
        tagList.forEach(tagRecord -> {
            Map<String, Object> tagFields = (Map<String, Object>) tagRecord;
            tagFields.entrySet().forEach((tagField) -> {
                if (!tagFieldNames.contains(tagField.getKey())) {
                    throw new ValidatorException(
                            ServiceExceptionCode.NON_EXISTING_TAG_FIELD_NAME.getErrorCode(), tagField.getKey());
                }
                if (tagField.getKey().equals(ServiceConstant.ID_FIELD.getValue())) {
                    validateCertificateUpdateFieldMatchesDataType(Integer.class, tagField);
                    validateIdIsPositive((Integer) tagField.getValue());
                }
                if (tagField.getKey().equals(ServiceConstant.NAME_FIELD.getValue())) {
                    validateCertificateUpdateFieldMatchesDataType(String.class, tagField);
                    validateStringField((String) tagField.getValue(), "tag name");
                }
            });
        });
    }

    private void trimAndLowerCaseValues(Map<String, String> params) {
        params.replaceAll((k, v) -> v.toLowerCase().trim());
    }

    private void checkCertificateParametersExist(Map<String, String> params) {
        params.forEach((key, value) -> {
            if (!certificateParameterNames.contains(key)) {
                throw new ValidatorException(ServiceExceptionCode.NON_EXISTING_PARAM_NAME.getErrorCode(), key);
            }
        });
    }

    private void checkOrderParametersExist(Map<String, String> params) {
        params.forEach((key, value) -> {
            if (!orderParameterNames.contains(key)) {
                throw new ValidatorException(ServiceExceptionCode.NON_EXISTING_PARAM_NAME.getErrorCode(), key);
            }
        });
    }

    private void checkParamsHaveOrderBy(Map<String, String> params) {
        if (params.containsKey(ServiceConstant.ORDER_BY_PARAM.getValue())) {
            validateOrderByValue(params.get(ServiceConstant.ORDER_BY_PARAM.getValue()));
        }
    }

    private void validateOrderByValue(String value) {
        if (!orderByValues.contains(value)) {
            throw new ValidatorException(ServiceExceptionCode.INVALID_ORDER_BY_VALUE.getErrorCode(), value);
        }
    }

    private void checkParamsHavePaginationParameters(Map<String, String> params) {
        validatePageParameter(params);
        validateSizeParameter(params);
    }

    private void validatePageParameter(Map<String, String> params) {
        if (!params.containsKey(ServiceConstant.PAGE_PARAM.getValue())) {
            params.put(ServiceConstant.PAGE_PARAM.getValue(), String.valueOf(DEFAULT_PAGE_NUMBER));
        } else {
            validatePageParameterValue(params.get(ServiceConstant.PAGE_PARAM.getValue()));
        }
    }

    private void validatePageParameterValue(String page) {
        if (NumberUtils.isParsable(page) && NumberUtils.createNumber(page).getClass().equals(Integer.class)) {
            int pageNumber = Integer.parseInt(page);
            if (pageNumber < 1) {
                throw new ValidatorException(ServiceExceptionCode.SHOULD_BE_POSITIVE.getErrorCode(), "page = " + page);
            }
        } else {
            throw new ValidatorException(ServiceExceptionCode.DATA_TYPE_DOES_NOT_MATCH_REQUIRED.getErrorCode(), page);
        }
    }

    private void validateSizeParameter(Map<String, String> params) {
        if (!params.containsKey(ServiceConstant.SIZE_PARAM.getValue())) {
            params.put(ServiceConstant.SIZE_PARAM.getValue(), String.valueOf(DEFAULT_SIZE));
        } else {
            validateSizeParameterValue(params.get(ServiceConstant.SIZE_PARAM.getValue()));
        }
    }

    private void validateSizeParameterValue(String size) {
        if (NumberUtils.isParsable(size) && NumberUtils.createNumber(size).getClass().equals(Integer.class)) {
            int pageNumber = Integer.parseInt(size);
            if (pageNumber < 1) {
                throw new ValidatorException(ServiceExceptionCode.SHOULD_BE_POSITIVE.getErrorCode(), "size = " + size);
            }
        } else {
            throw new ValidatorException(ServiceExceptionCode.DATA_TYPE_DOES_NOT_MATCH_REQUIRED.getErrorCode(), size);
        }
    }
}
