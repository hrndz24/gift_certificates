package com.epam.esm.validation;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ValidatorException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class Validator {

    public void validateCertificate(GiftCertificate certificate) {
        checkNonNull(certificate, certificate.getClass().getName());
        validateStringField(certificate.getName(), "name");
        validateStringField(certificate.getDescription(), "description");
        validatePrice(certificate.getPrice());
        validateDateField(certificate.getCreateDate(), "create date");
        validateDateField(certificate.getLastUpdateDate(), "last update date");
        validateDuration(certificate.getDuration());
    }

    public void validateTag(Tag tag) {
        checkNonNull(tag, tag.getClass().getName());
        validateStringField(tag.getName(), "name");
    }

    private void checkNonNull(Object object, String className) {
        if (object == null) {
            throw new ValidatorException("Null " + className);
        }
    }

    private void validateStringField(String string, String field) {
        if (string == null) {
            throw new ValidatorException("Null certificate" + field);
        }
        if (string.isEmpty()) {
            throw new ValidatorException("Empty certificate " + field);
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
}
