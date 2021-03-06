package com.epam.esm.dao.impl;

import com.epam.esm.exception.DAOException;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DAOTestConfig.class)
@SpringBootTest
@Transactional
class TagDAOImplTest {

    @Autowired
    private TagDAOImpl tagDAO;

    private Tag existentTag;

    @BeforeEach
    void setUp() {
        createExistentTag();
    }

    private void createExistentTag() {
        existentTag = new Tag();
        existentTag.setId(1);
        existentTag.setName("entertainment");
    }

    @Test
    void addTagWithNewTagShouldAddTag() {
        Tag newTag = new Tag();
        newTag.setName("beauty");
        tagDAO.addTag(newTag);
        assertNotEquals(0, newTag.getId());
        assertEquals(4, tagDAO.getTags(10, 0).size());
    }

    @Test
    void addTagWithExistentIdShouldThrowException() {
        assertThrows(DAOException.class, () -> tagDAO.addTag(existentTag));
    }

    @Test
    void removeTagWithoutAssignedCertificateShouldRemoveTag() {
        tagDAO.removeTag(3);
        assertEquals(2, tagDAO.getTags(10, 0).size());
    }

    @Test
    void getTagsShouldReturnListOfThreeTags() {
        assertEquals(3, tagDAO.getTags(10, 0).size());
    }

    @Test
    void getTagByIdWithExistentIdShouldReturnTag() {
        assertEquals(existentTag, tagDAO.getTagById(1));
    }

    @Test
    void getTagByIdWithNonExistentIdShouldReturnNull() {
        assertNull(tagDAO.getTagById(4));
    }

}
