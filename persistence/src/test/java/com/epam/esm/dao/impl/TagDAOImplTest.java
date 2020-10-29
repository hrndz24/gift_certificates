package com.epam.esm.dao.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DAOException;
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
    }

    @Test
    void addTag_TagWithExistentId() {
        assertThrows(DAOException.class, () -> tagDAO.addTag(existentTag));
    }

    @Test
    void removeTag_WithoutAssignedCertificate() {
        Tag tag = new Tag();
        tag.setId(3);
        tagDAO.removeTag(tag);
        assertEquals(2, tagDAO.getTags().size());
    }

    @Test
    void removeTag_WithAssignedCertificate() {
        assertThrows(DAOException.class, () -> tagDAO.removeTag(existentTag));
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
        assertThrows(DAOException.class, () -> tagDAO.getTagById(4));
    }

    @Test
    void getTagByName_ExistentName() {
        assertEquals(existentTag, tagDAO.getTagByName("entertainment"));
    }

    @Test
    void getTagByName_NonExistentName() {
        assertThrows(DAOException.class, () -> tagDAO.getTagByName("swan"));
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }
}