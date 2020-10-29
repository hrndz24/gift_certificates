package com.epam.esm.dao;

import com.epam.esm.model.Tag;

import java.util.List;

public interface TagDAO {

    void addTag(Tag tag);

    void removeTag(int tagId);

    List<Tag> getTags();

    Tag getTagById(int id);

    Tag getTagByName(String name);
}
