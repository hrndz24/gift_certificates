package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagServiceImpl implements TagService {

    private TagDAO tagDAO;
    private Validator validator;

    @Autowired
    public TagServiceImpl(TagDAO tagDAO, Validator validator) {
        this.tagDAO = tagDAO;
        this.validator = validator;
    }

    @Override
    public void addTag(Tag tag) {
        validator.validateTag(tag);
        tagDAO.addTag(tag);
    }

    @Override
    public void removeTag(Tag tag) {
        tagDAO.removeTag(tag);
    }

    @Override
    public List<Tag> getTags() {
        return tagDAO.getTags();
    }

    @Override
    public Tag getTagById(int id) {
        return tagDAO.getTagById(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagDAO.getTagByName(name);
    }
}
