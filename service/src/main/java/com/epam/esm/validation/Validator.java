package com.epam.esm.validation;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.exception.ValidatorException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Component
public class Validator {

    private Set<String> certificateParameterNames;
    private Set<String> orderParameterNames;
    private Set<String> orderByValues;
    private Set<String> certificateFieldNames;
    private Set<String> tagFieldNames;

    public Validator() {
        this.certificateParameterNames = new HashSet<>();
        this.orderParameterNames = new HashSet<>();
        this.orderByValues = new HashSet<>();
        this.certificateFieldNames = new HashSet<>();
        this.tagFieldNames = new HashSet<>();
        fillInParameterNames();
    }

    private void fillInParameterNames() {
        certificateParameterNames.add("certificateName");
        certificateParameterNames.add("certificateDescription");
        certificateParameterNames.add("tagName");
        certificateParameterNames.add("orderBy");
        orderParameterNames.add("userId");
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

    public void validateOrder(OrderDTO orderDTO) {
        validateNonNull(orderDTO, OrderDTO.class.getName());
        checkIdIsPositive(orderDTO.getUserId());
        checkOrderHasCertificates(orderDTO);
    }

    public void validateCertificateForOrdering(GiftCertificateDTO certificate) {
        validateNonNull(certificate, GiftCertificateDTO.class.getName());
        checkIdIsPositive(certificate.getId());
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

    public void validateCertificateParams(Map<String, String> params) {
        validateNonNull(params, params.getClass().getName());
        trimAndLowerCaseValues(params);
        checkCertificateParametersExist(params);
        checkParamsHaveOrderBy(params);
        checkParamsHaveTagName(params);
    }

    private void checkParamsHaveTagName(Map<String, String> params) {
        if (params.containsKey("tagName")) {
            params.replace("tagName", validateTagNames(params.get("tagName")));
        }
    }

    private String validateTagNames(String tagNamesAsString) {
        List<String> validatedTags = new ArrayList<>();
        String[] tagNames = tagNamesAsString.split(",");
        for (String tagName : tagNames) {
            validateStringField(tagName, "tag name");
            validatedTags.add(tagName.trim().toLowerCase());
        }
        return String.join(", ", validatedTags.toArray(new String[0]));
    }

    public void validateOrderParams(Map<String, String> params) {
        validateNonNull(params, params.getClass().getName());
        trimAndLowerCaseValues(params);
        checkOrderParametersExist(params);
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
                case "name":
                    validateCertificateUpdateFieldMatchesDataType(String.class, entry);
                    validateStringField((String) entry.getValue(), "certificate name");
                    break;
                case "description":
                    validateCertificateUpdateFieldMatchesDataType(String.class, entry);
                    validateStringField((String) entry.getValue(), "certificate description");
                    break;
                case "price":
                    validateCertificateUpdateFieldMatchesDataType(Double.class, entry);
                    validatePrice((BigDecimal) entry.getValue());
                    break;
                case "duration":
                    validateCertificateUpdateFieldMatchesDataType(Integer.class, entry);
                    validateDuration((Integer) entry.getValue());
                    break;
                case "tags":
                    System.out.println(entry.getValue());
                    System.out.println(entry.getValue().getClass().getName());
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
        List<GiftCertificateDTO> certificates = order.getCertificates();
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
                if (tagField.getKey().equals("id")) {
                    validateCertificateUpdateFieldMatchesDataType(Integer.class, tagField);
                    checkIdIsPositive((Integer) tagField.getValue());
                }
                if (tagField.getKey().equals("name")) {
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
