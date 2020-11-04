package com.epam.esm.dao.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CertificateTagDAOImplTest {

    private EmbeddedDatabase embeddedDatabase;

    private CertificateTagDAOImpl certificateTagDAO;
    private GiftCertificateDAOImpl giftCertificateDAO;

    @BeforeEach
    void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        certificateTagDAO = new CertificateTagDAOImpl(jdbcTemplate);
        giftCertificateDAO = new GiftCertificateDAOImpl(jdbcTemplate);
    }

    @Test
    void addTagToCertificateShouldAddTag() {
        certificateTagDAO.addTagToCertificate(1, 2);
        assertEquals(2, giftCertificateDAO.getCertificateById(1).getTags().size());
    }

    @Test
    void removeTagFromCertificateShouldRemoveTag() {
        certificateTagDAO.removeTagFromCertificate(2, 2);
        assertEquals(1, giftCertificateDAO.getCertificateById(2).getTags().size());
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }
}
