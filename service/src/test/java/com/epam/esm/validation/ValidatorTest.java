package com.epam.esm.validation;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.ValidatorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorTest {

    private Validator validator;

    private GiftCertificateDTO validGiftCertificate;
    private TagDTO validTag;
    private Map<String, String> params;

    @BeforeEach
    public void setUp() throws ParseException {
        validator = new Validator();
        createValidCertificate();
        createValidTag();
        createValidParams();
    }

    private void createValidCertificate() throws ParseException {
        validGiftCertificate = new GiftCertificateDTO();
        validGiftCertificate.setId(1);
        validGiftCertificate.setName("Disney Land");
        validGiftCertificate.setDescription("Needs no description cuz it is Disney Land");
        validGiftCertificate.setPrice(new BigDecimal("12.00"));
        validGiftCertificate.setCreateDate(new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse("2020-10-27 21:17:24"));
        validGiftCertificate.setLastUpdateDate(new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse("2020-10-27 21:17:24"));
        validGiftCertificate.setDuration(120);
        TagDTO tag = new TagDTO();
        tag.setId(1);
        tag.setName("entertainment");
        validGiftCertificate.getTags().add(tag);
    }

    private void createValidTag() {
        validTag = new TagDTO();
        validTag.setId(1);
        validTag.setName("entertainment");
    }

    private void createValidParams() {
        params = new HashMap<>();
        params.put("certificateName", "Dis");
        params.put("orderBy", "name");
    }

    @Test
    void validateCertificate() {
        validator.validateCertificate(validGiftCertificate);
    }

    @Test
    void validateCertificateWithNullName() {
        validGiftCertificate.setName(null);
        assertThrows(ValidatorException.class,
                () -> validator.validateCertificate(validGiftCertificate));
    }

    @Test
    void validateCertificateWithEmptyDescription() {
        validGiftCertificate.setDescription("");
        assertThrows(ValidatorException.class,
                () -> validator.validateCertificate(validGiftCertificate));
    }

    @Test
    void validateCertificateWithNegativePrice() {
        validGiftCertificate.setPrice(new BigDecimal("-12.00"));
        assertThrows(ValidatorException.class,
                () -> validator.validateCertificate(validGiftCertificate));
    }

    @Test
    void validateCertificateWithNegativeDuration() {
        validGiftCertificate.setDuration(-12);
        assertThrows(ValidatorException.class,
                () -> validator.validateCertificate(validGiftCertificate));
    }

    @Test
    void validateTag() {
        validator.validateTag(validTag);
    }

    @Test
    void validateTag_NullName() {
        validTag.setName(null);
        assertThrows(ValidatorException.class,
                () -> validator.validateTag(validTag));
    }

    @Test
    void validateParams() {
        validator.validateParams(params);
    }

    @Test
    void validateParams_NonExistentParam() {
        params.put("nonExistent", "alpha");
        assertThrows(ValidatorException.class, () -> validator.validateParams(params));
    }

    @Test
    void validateParams_NonExistentOrderByValue() {
        params.put("orderBy", "alpha");
        assertThrows(ValidatorException.class,
                () -> validator.validateParams(params));
    }
}