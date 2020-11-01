package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.service.TagService;
import com.epam.esm.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagServiceImpl implements TagService {

    private TagDAO tagDAO;
    private Validator validator;
    private TagMapper mapper;

    @Autowired
    public TagServiceImpl(TagDAO tagDAO, Validator validator, TagMapper mapper) {
        this.tagDAO = tagDAO;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public TagDTO addTag(TagDTO tagDTO) {
        validator.validateTag(tagDTO);
        return mapper.toDTO(tagDAO.addTag(mapper.toModel(tagDTO)));
    }

    @Override
    public void removeTag(int tagId) {
        tagDAO.removeTag(tagId);
    }

    @Override
    public List<TagDTO> getTags() {
        List<TagDTO> tags = new ArrayList<>();
        tagDAO.getTags().forEach(tag -> {
            tags.add(mapper.toDTO(tag));
        });
        return tags;
    }

    @Override
    public TagDTO getTagById(int id) {
        return mapper.toDTO(tagDAO.getTagById(id));
    }
}
