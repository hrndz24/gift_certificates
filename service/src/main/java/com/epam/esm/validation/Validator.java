package com.epam.esm.validation;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.exception.ValidatorException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
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
        checkNonNull(giftCertificateDTO, giftCertificateDTO.getClass().getName());
        validateStringField(giftCertificateDTO.getName(), " certificate name");
        validateStringField(giftCertificateDTO.getDescription(), "description");
        validatePrice(giftCertificateDTO.getPrice());
        validateDateField(giftCertificateDTO.getCreateDate(), "create date");
        validateDateField(giftCertificateDTO.getLastUpdateDate(), "last update date");
        validateDuration(giftCertificateDTO.getDuration());
    }

    public void validateTag(TagDTO tagDTO) {
        checkNonNull(tagDTO, tagDTO.getClass().getName());
        validateStringField(tagDTO.getName(), "tag name");
    }

    private void checkNonNull(Object object, String className) {
        if (object == null) {
            throw new ValidatorException("Null " + className);
        }
    }

    private void validateStringField(String string, String field) {
        if (string == null) {
            throw new ValidatorException("Null " + field);
        }
        if (string.isEmpty()) {
            throw new ValidatorException("Empty " + field);
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new ValidatorException("Null certificate price");
        }
        if (price.doubleValue() <= 0) {
            throw new ValidatorException("Negative certificate price");
        }
    }

    private void validateDateField(Date date, String field) {
        if (date == null) {
            throw new ValidatorException("Null certificate " + field);
        }
        if (date.after(new Date())) {
            throw new ValidatorException("Future certificate " + field);
        }
    }

    private void validateDuration(int duration) {
        if (duration <= 0) {
            throw new ValidatorException("Negative certificate duration");
        }
    }

    public void validateParams(Map<String, String> params) {
        checkNonNull(params, params.getClass().getName());
        removeNonExistentParameterNames(params);
        checkParamsHaveOrderBy(params);
    }

    private void removeNonExistentParameterNames(Map<String, String> params) {
        params.entrySet().removeIf(
                entry -> !parameterNames.contains(entry.getKey()));
    }

    private void checkParamsHaveOrderBy(Map<String, String> params) {
        if (params.containsKey("orderBy")) {
            validateOrderByValue(params.get("orderBy"));
        }
    }

    private void validateOrderByValue(String value) {
        if (!orderByValues.contains(value)) {
            throw new ValidatorException("Invalid orderBy value");
        }
    }
}
