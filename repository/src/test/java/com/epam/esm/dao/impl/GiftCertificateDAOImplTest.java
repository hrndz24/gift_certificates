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
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GiftCertificateDAOImplTest {

    private EmbeddedDatabase embeddedDatabase;

    private GiftCertificateDAOImpl giftCertificateDAO;

    private GiftCertificate existentCertificate;

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
        existentCertificate.setCreateDate(new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse("2020-10-27 21:17:24"));
        existentCertificate.setLastUpdateDate(new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse("2020-10-27 21:17:24"));
        existentCertificate.setDuration(120);
        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("entertainment");
        existentCertificate.addTag(tag);
    }

    @Test
    void addGiftCertificate() {
        GiftCertificate newCertificate = new GiftCertificate();
        newCertificate.setName("SPA");
        newCertificate.setDescription("Beauty for everyone");
        newCertificate.setPrice(new BigDecimal("100.00"));
        newCertificate.setDuration(10);
        giftCertificateDAO.addCertificate(newCertificate);
        assertNotEquals(0, newCertificate.getId());
        assertEquals(4, giftCertificateDAO.getCertificates().size());
    }

    @Test
    void removeGiftCertificate() {
        giftCertificateDAO.removeCertificate(existentCertificate);
        assertEquals(2, giftCertificateDAO.getCertificates().size());
    }

    @Test
    void updateGiftCertificate() {
        GiftCertificate certificate = giftCertificateDAO.getCertificateById(1);
        certificate.setName("Not Disney Land anymore");
        giftCertificateDAO.updateCertificate(certificate);
        assertEquals(certificate, giftCertificateDAO.getCertificateById(1));
    }

    @Test
    void addTagToCertificate() {
        giftCertificateDAO.addTagToCertificate(1, 2);
        assertEquals(2, giftCertificateDAO.getCertificateById(1).getTags().size());
    }

    @Test
    void removeTagFromCertificate() {
        giftCertificateDAO.removeTagFromCertificate(2, 2);
        assertEquals(1, giftCertificateDAO.getCertificateById(2).getTags().size());
    }

    @Test
    void getGiftCertificates() {
        assertEquals(3, giftCertificateDAO.getCertificates().size());
    }

    @Test
    void getGiftCertificateById() {
        assertEquals(existentCertificate, giftCertificateDAO.getCertificateById(1));
    }

    @Test
    void getGiftCertificatesByTagName() {
        assertEquals(1, giftCertificateDAO.getCertificatesByTagName("geek").size());
    }

    @Test
    void getGiftCertificatesByName() {
        assertEquals(1, giftCertificateDAO.getCertificatesByName("Dis").size());
    }

    @Test
    void getGiftCertificatesByDescription() {
        assertEquals(1, giftCertificateDAO.getCertificatesByDescription("About").size());
    }

    @Test
    void getGiftCertificatesSortedByDateAscending() {
        assertEquals(existentCertificate, giftCertificateDAO.getCertificatesSortedByDateAscending().get(2));
    }

    @Test
    void getGiftCertificatesSortedByDateDescending() {
        assertEquals(existentCertificate, giftCertificateDAO.getCertificatesSortedByDateDescending().get(0));
    }

    @Test
    void getGiftCertificatesSortedByNameAscending() {
        assertEquals(existentCertificate, giftCertificateDAO.getCertificatesSortedByNameAscending().get(1));
    }

    @Test
    void getGiftCertificatesSortedByNameDescending() {
        assertEquals(existentCertificate, giftCertificateDAO.getCertificatesSortedByNameDescending().get(1));
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }
}