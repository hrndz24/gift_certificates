package com.epam.esm.validation;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ValidatorException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class GiftCertificateValidator {

    public void validate(GiftCertificate certificate) throws ValidatorException {
        checkNonNull(certificate);
        validateStringField(certificate.getName(), "name");
        validateStringField(certificate.getDescription(), "description");
        validatePrice(certificate.getPrice());
        validateDateField(certificate.getCreateDate(), "create date");
        validateDateField(certificate.getLastUpdateDate(), "last update date");
        validateDuration(certificate.getDuration());
    }

    private void checkNonNull(GiftCertificate certificate) throws ValidatorException {
        if (certificate == null) {
            throw new ValidatorException("Null certificate");
        }
    }

    private void validateStringField(String string, String field) throws ValidatorException {
        if (string == null) {
            throw new ValidatorException("Null certificate" + field);
        }
        if (string.isEmpty()) {
            throw new ValidatorException("Empty certificate " + field);
        }
    }

    private void validatePrice(BigDecimal price) throws ValidatorException {
        if (price == null) {
            throw new ValidatorException("Null certificate price");
        }
        if (price.doubleValue() <= 0) {
            throw new ValidatorException("Negative certificate price");
        }
    }

    private void validateDateField(Date date, String field) throws ValidatorException {
        if (date == null) {
            throw new ValidatorException("Null certificate " + field);
        }
        if (date.after(new Date())) {
            throw new ValidatorException("Future certificate " + field);
        }
    }

    private void validateDuration(int duration) throws ValidatorException {
        if (duration <= 0) {
            throw new ValidatorException("Negative certificate duration");
        }
    }
}
