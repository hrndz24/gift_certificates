package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagService {

    void addTag(Tag tag);

    void removeTag(Tag tag);

    List<Tag> getTags();

    Tag getTagById(int id);

    Tag getTagByName(String name);
}
