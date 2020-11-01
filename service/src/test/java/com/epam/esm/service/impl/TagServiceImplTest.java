package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.Tag;
import com.epam.esm.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class TagServiceImplTest {

    private TagServiceImpl tagService;
    private TagDAO tagDAO;
    private Validator validator;
    private TagMapper mapper;

    @BeforeEach
    void setUp() {
        tagDAO = Mockito.mock(TagDAO.class);
        validator = Mockito.mock(Validator.class);
        mapper = Mockito.mock(TagMapper.class);
        tagService = new TagServiceImpl(tagDAO, validator, mapper);
    }

    @Test
    void addTag() {
        /*Tag tag = new Tag();
        tag.setName("name");
        when(tagDAO.addTag(new Tag())).thenAnswer(new Answer<Tag>() {
            @Override
            public Tag answer(InvocationOnMock invocation) throws Throwable {
                Tag addedTag = new Tag();
                addedTag.setName(invocation.getArgument(1));
                addedTag.setId(24);
                return addedTag;
            }
        });
        TagDTO tagDTOtoAdd = new TagDTO();
        tagDTOtoAdd.setName("name");
        when(mapper.toModel(tagDTOtoAdd)).thenReturn(tag);
        tag.setId(24);
        tagDTOtoAdd.setId(24);
        when(mapper.toDTO(tag)).thenReturn(tagDTOtoAdd);
        TagDTO returnedTagDTO = tagService.addTag(tagDTOtoAdd);
        assertEquals(returnedTagDTO.getName(), "name");
        assertEquals(returnedTagDTO.getId(), 24);*/
    }

    @Test
    void removeTag() {
        doNothing().when(tagDAO).removeTag(anyInt());
        tagService.removeTag(anyInt());
    }

    @Test
    void getTags() {
        List<TagDTO> tags = new ArrayList<>();
        tags.add(new TagDTO());
        tags.add(new TagDTO());
        //when(tagDAO.getTags()).thenReturn(tags);
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
}