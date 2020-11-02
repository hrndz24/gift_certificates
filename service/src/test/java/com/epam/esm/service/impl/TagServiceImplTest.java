package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ValidatorException;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.Tag;
import com.epam.esm.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl tagService;
    @Mock
    private TagDAO tagDAO;
    @Mock
    private Validator validator;
    @Mock
    private TagMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addTag() {
        String name = "newTag";
        Tag tagToAdd = new Tag();
        tagToAdd.setName(name);
        Tag tagReturned = new Tag();
        tagReturned.setName(tagToAdd.getName());
        tagReturned.setId(24);
        when(tagDAO.addTag(tagToAdd)).thenReturn(tagReturned);
        TagDTO tagDTOToAdd = new TagDTO();
        tagDTOToAdd.setName(tagToAdd.getName());
        TagDTO tagDTOReturned = new TagDTO();
        tagDTOReturned.setName(tagDTOToAdd.getName());
        tagDTOReturned.setId(24);
        doNothing().when(validator).validateTag(tagDTOToAdd);
        when(mapper.toModel(tagDTOToAdd)).thenReturn(tagToAdd);
        when(mapper.toDTO(tagReturned)).thenReturn(tagDTOReturned);
        assertEquals(tagDTOReturned.getId(), tagReturned.getId());
    }

    @Test
    void addTag_NullName() {
        TagDTO tagDTOToAdd = new TagDTO();
        tagDTOToAdd.setName(null);
        doThrow(ValidatorException.class).when(validator).validateTag(tagDTOToAdd);
        assertThrows(ValidatorException.class, () -> tagService.addTag(tagDTOToAdd));
    }

    @Test
    void removeTag() {
        doNothing().when(tagDAO).removeTag(anyInt());
        tagService.removeTag(anyInt());
    }

    @Test
    void getTags() {
        Tag tag1 = new Tag();
        tag1.setId(1);
        tag1.setName("tag1");
        Tag tag2 = new Tag();
        tag2.setId(2);
        tag2.setName("tag2");
        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);

        TagDTO tagDTO1 = new TagDTO();
        tagDTO1.setId(1);
        tagDTO1.setName("tag1");
        TagDTO tagDTO2 = new TagDTO();
        tagDTO2.setId(2);
        tagDTO2.setName("tag2");
        List<TagDTO> tagDTOs = new ArrayList<>();
        tagDTOs.add(tagDTO1);
        tagDTOs.add(tagDTO2);
        when(tagDAO.getTags()).thenReturn(tags);
        when(mapper.toDTO(tag1)).thenReturn(tagDTO1);
        when(mapper.toDTO(tag2)).thenReturn(tagDTO2);
        Assertions.assertEquals(tagDTOs, tagService.getTags());
    }

    @Test
    void getTagById() {
        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("entertainment");
        when(tagDAO.getTagById(1)).thenReturn(tag);
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(1);
        tagDTO.setName("entertainment");
        when(mapper.toDTO(tag)).thenReturn(tagDTO);
        TagDTO returnedTag = tagService.getTagById(1);
        assertEquals(tag.getName(), returnedTag.getName());
    }

    @Test
    void getTagById_NonExistentId() {
        Tag tag = new Tag();
        tag.setId(24);
        when(tagDAO.getTagById(24)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> tagService.getTagById(24));
    }
}