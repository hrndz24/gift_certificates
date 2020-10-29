package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Tag;
import com.epam.esm.validation.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TagServiceImplTest {

    private TagServiceImpl tagService;
    private TagDAO tagDAO;
    private TagValidator validator;

    @BeforeEach
    void setUp() {
        tagDAO = mock(TagDAO.class);
        validator = mock(TagValidator.class);
        tagService = new TagServiceImpl(tagDAO, validator);
    }

    @Test
    void addTag() {
        Tag tag = new Tag();
    }

    @Test
    void removeTag() {
    }

    @Test
    void getTags() {
    }

    @Test
    void getTagById() {
    }

    @Test
    void getTagByName() {
    }
}