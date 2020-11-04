package com.epam.esm.dao.impl;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GiftCertificateDAOImplTest {

    private EmbeddedDatabase embeddedDatabase;

    private GiftCertificateDAOImpl giftCertificateDAO;

    private GiftCertificate existentCertificate;

    private String queryConditionToGetAllTags = "";

    @BeforeEach
    void setUp() throws ParseException {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        giftCertificateDAO = new GiftCertificateDAOImpl(jdbcTemplate);
        createExistentCertificate();
    }

    private void createExistentCertificate() throws ParseException {
        existentCertificate = new GiftCertificate();
        existentCertificate.setId(1);
        existentCertificate.setName("Disney Land");
        existentCertificate.setDescription("Needs no description cuz it is Disney Land");
        existentCertificate.setPrice(new BigDecimal("12.00"));
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                .parse("2020-10-27 21:17:24");
        existentCertificate.setCreateDate(date);
        existentCertificate.setLastUpdateDate(date);
        existentCertificate.setDuration(120);
        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("entertainment");
        existentCertificate.addTag(tag);
    }

    @Test
    void addGiftCertificateWithNewCertificateShouldAddCertificate() {
        GiftCertificate newCertificate = new GiftCertificate();
        newCertificate.setName("SPA");
        newCertificate.setDescription("Beauty for everyone");
        newCertificate.setPrice(new BigDecimal("100.00"));
        newCertificate.setDuration(10);
        giftCertificateDAO.addCertificate(newCertificate);
        assertNotEquals(0, newCertificate.getId());
        assertEquals(4, giftCertificateDAO.getCertificates(queryConditionToGetAllTags).size());
    }

    @Test
    void removeGiftCertificateShouldRemoveCertificate() {
        giftCertificateDAO.removeCertificate(1);
        assertEquals(2, giftCertificateDAO.getCertificates(queryConditionToGetAllTags).size());
    }

    @Test
    void updateGiftCertificateShouldUpdateCertificate() {
        GiftCertificate certificate = giftCertificateDAO.getCertificateById(1);
        certificate.setName("Not Disney Land anymore");
        giftCertificateDAO.updateCertificate(certificate);
        assertEquals(certificate, giftCertificateDAO.getCertificateById(1));
    }

    @Test
    void getGiftCertificatesShouldReturnListOfThreeCertificates() {
        assertEquals(3, giftCertificateDAO.getCertificates(queryConditionToGetAllTags).size());
    }

    @Test
    void getGiftCertificateByIdShouldReturnCertificate() {
        assertEquals(existentCertificate, giftCertificateDAO.getCertificateById(1));
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }
}
