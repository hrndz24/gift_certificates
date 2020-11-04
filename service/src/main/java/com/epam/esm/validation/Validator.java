package com.epam.esm.validation;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.exception.ValidatorException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class Validator {

    private Set<String> parameterNames;
    private Set<String> orderByValues;

    public Validator() {
        this.parameterNames = new HashSet<>();
        this.orderByValues = new HashSet<>();
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
                    ServiceExceptionCode.NEGATIVE.getErrorCode(), "id = " + id);
        }
    }

    private void validateNonNull(Object object, String className) {
        if (object == null) {
            throw new ValidatorException(ServiceExceptionCode.NULL.getErrorCode(), className);
        }
    }

    private void validateStringField(String string, String field) {
        if (StringUtils.isEmpty(string)) {
            throw new ValidatorException(ServiceExceptionCode.EMPTY.getErrorCode(), field);
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new ValidatorException(ServiceExceptionCode.NULL.getErrorCode(), "price");
        }
        if (price.doubleValue() <= 0) {
            throw new ValidatorException(ServiceExceptionCode.NEGATIVE.getErrorCode(), "price = " + price);
        }
    }

    private void validateDuration(int duration) {
        if (duration <= 0) {
            throw new ValidatorException(ServiceExceptionCode.NEGATIVE.getErrorCode(), "duration = " + duration);
        }
    }

    public void validateParams(Map<String, String> params) {
        validateNonNull(params, params.getClass().getName());
        checkAllParametersExist(params);
        checkParamsHaveOrderBy(params);
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
