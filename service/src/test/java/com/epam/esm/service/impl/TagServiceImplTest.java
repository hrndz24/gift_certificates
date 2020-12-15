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
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl tagService;
    @Mock
    private TagDAO tagDAO;
    @Spy
    private Validator validator = new Validator();
    @Spy
    private TagMapper mapper = new TagMapper(new ModelMapper());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addTagWithValidParamsShouldAddTag() {
        String name = "newTag";
        Tag tagToAdd = new Tag();
        tagToAdd.setName(name);
        Tag tagReturned = new Tag();
        tagReturned.setName(tagToAdd.getName());
        tagReturned.setId(24);
        when(tagDAO.addTag(tagToAdd)).thenReturn(tagReturned);
        TagDTO tagDTOToAdd = new TagDTO();
        tagDTOToAdd.setName(tagToAdd.getName());
        TagDTO tagDTOReturned = tagService.addTag(tagDTOToAdd);
        doNothing().when(validator).validateTag(tagDTOToAdd);
        assertEquals(tagDTOReturned.getId(), tagReturned.getId());
    }

    @Test
    void addTagWithNullNameShouldThrowException() {
        TagDTO tagDTOToAdd = new TagDTO();
        tagDTOToAdd.setName(null);
        assertThrows(ValidatorException.class, () -> tagService.addTag(tagDTOToAdd));
    }

    @Test
    void removeTagShouldRemoveTag() {
        doNothing().when(tagDAO).removeTag(3);
        when(tagDAO.getTagById(3)).thenReturn(new Tag());
        tagService.removeTag(3);
    }

    @Test
    void getTagsShouldReturnTags() {
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
        when(tagDAO.getTags(10, 0)).thenReturn(tags);
        doNothing().when(validator).validatePageNumberIsLessThanElementsCount(anyMap(), anyLong());
        Assertions.assertEquals(tagDTOs, tagService.getTags(new HashMap<>()).getTags());
    }

    @Test
    void getTagByIdWithExistentIdShouldReturnTag() {
        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("entertainment");
        when(tagDAO.getTagById(1)).thenReturn(tag);
        TagDTO returnedTag = tagService.getTagById(1);
        assertEquals(tag.getName(), returnedTag.getName());
    }

    @Test
    void getTagByIdWithNonExistentIdShouldThrowException() {
        Tag tag = new Tag();
        tag.setId(24);
        when(tagDAO.getTagById(24)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> tagService.getTagById(24));
    }
}
