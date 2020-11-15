package com.epam.esm.validation;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.exception.ValidatorException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Component
public class Validator {

    private Set<String> parameterNames;
    private Set<String> orderByValues;
    private Set<String> certificateFieldNames;
    private Set<String> tagFieldNames;

    public Validator() {
        this.parameterNames = new HashSet<>();
        this.orderByValues = new HashSet<>();
        this.certificateFieldNames = new HashSet<>();
        this.tagFieldNames = new HashSet<>();
        fillInParameterNames();
    }

    private void fillInParameterNames() {
        parameterNames.add("certificateName");
        parameterNames.add("certificateDescription");
        parameterNames.add("tagName");
        parameterNames.add("orderBy");
        orderByValues.add("name");
        orderByValues.add("-name");
        orderByValues.add("date");
        orderByValues.add("-date");
        certificateFieldNames.add("name");
        certificateFieldNames.add("description");
        certificateFieldNames.add("price");
        certificateFieldNames.add("duration");
        certificateFieldNames.add("tags");
        tagFieldNames.add("id");
        tagFieldNames.add("name");
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

    public void checkIdIsPositive(int id) {
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

    public void validateParams(Map<String, String> params) {
        validateNonNull(params, params.getClass().getName());
        trimAndLowerCaseValues(params);
        checkAllParametersExist(params);
        checkParamsHaveOrderBy(params);
    }

    public void validateCertificateUpdateFields(Map<String, Object> fields) {
        validateNonNull(fields, fields.getClass().getName());
        checkCertificateFieldsExist(fields);
        checkFieldValuesNonNull(fields);
        checkFieldsHaveTagsField(fields);
    }

    public void validateCertificateUpdateFieldMatchesDataType(Class<?> requested, Map.Entry<String, Object> field) {
        if (requested != field.getValue().getClass()) {
            throw new ValidatorException(
                    ServiceExceptionCode.DATA_TYPE_DOES_NOT_MATCH_REQUIRED.getErrorCode(), field.getKey());
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

    private void checkFieldValuesNonNull(Map<String, Object> fields) {
        fields.forEach((key, value) -> validateNonNull(value, key));
    }

    private void checkFieldsHaveTagsField(Map<String, Object> fields) {
        if (fields.containsKey("tags")) {
            validateTagsField(fields.get("tags"));
        }
    }

    private void validateTagsField(Object tagsValue) {
        List<?> tagList = (ArrayList<?>) tagsValue;
        tagList.forEach(tagRecord -> {
            Map<String, Object> tagFields = (Map<String, Object>) tagRecord;
            tagFields.entrySet().forEach((tagField) -> {
                if (!tagFieldNames.contains(tagField.getKey())) {
                    throw new ValidatorException(
                            ServiceExceptionCode.NON_EXISTING_TAG_FIELD_NAME.getErrorCode(), tagField.getKey());
                }
                if (tagField.getKey().equals("id"))
                    validateCertificateUpdateFieldMatchesDataType(Integer.class, tagField);
                if (tagField.getKey().equals("name"))
                    validateCertificateUpdateFieldMatchesDataType(String.class, tagField);
            });
        });
    }

    private void trimAndLowerCaseValues(Map<String, String> params) {
        params.replaceAll((k, v) -> v.toLowerCase().trim());
    }

    private void checkAllParametersExist(Map<String, String> params) {
        params.forEach((key, value) -> {
            if (!parameterNames.contains(key)) {
                throw new ValidatorException(ServiceExceptionCode.NON_EXISTING_PARAM_NAME.getErrorCode(), key);
            }
        });
    }

    private void checkParamsHaveOrderBy(Map<String, String> params) {
        if (params.containsKey("orderBy")) {
            validateOrderByValue(params.get("orderBy"));
        }
    }

    private void validateOrderByValue(String value) {
        if (!orderByValues.contains(value)) {
            throw new ValidatorException(ServiceExceptionCode.INVALID_ORDER_BY_VALUE.getErrorCode(), value);
        }
    }
}
