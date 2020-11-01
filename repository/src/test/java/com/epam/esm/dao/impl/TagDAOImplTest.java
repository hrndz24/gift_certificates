package com.epam.esm.dao.impl;

import com.epam.esm.exception.DAOException;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.junit.jupiter.api.Assertions.*;

class TagDAOImplTest {

    private EmbeddedDatabase embeddedDatabase;

    private TagDAOImpl tagDAO;

    private Tag existentTag;

    @BeforeEach
    void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        tagDAO = new TagDAOImpl(jdbcTemplate);
        createExistentTag();
    }

    private void createExistentTag() {
        existentTag = new Tag();
        existentTag.setId(1);
        existentTag.setName("entertainment");
    }

    @Test
    void addTag_NewTag() {
        Tag newTag = new Tag();
        newTag.setName("beauty");
        tagDAO.addTag(newTag);
        assertNotEquals(0, newTag.getId());
        assertEquals(4, tagDAO.getTags().size());
    }

    @Test
    void addTag_TagWithExistentId() {
        assertThrows(DAOException.class, () -> tagDAO.addTag(existentTag));
    }

    @Test
    void removeTag_WithoutAssignedCertificate() {
        tagDAO.removeTag(3);
        assertEquals(2, tagDAO.getTags().size());
    }

    @Test
    void removeTag_WithAssignedCertificate() {
        assertThrows(DAOException.class, () -> tagDAO.removeTag(1));
    }

    @Test
    void getTags() {
        assertEquals(3, tagDAO.getTags().size());
    }

    @Test
    void getTagById_ExistentId() {
        assertEquals(existentTag, tagDAO.getTagById(1));
    }

    @Test
    void getTagById_NonExistentId() {
        assertNull(tagDAO.getTagById(4));
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }
}