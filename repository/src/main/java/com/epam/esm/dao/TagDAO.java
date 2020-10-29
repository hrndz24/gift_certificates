package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagDAO {

    void addTag(Tag tag);

    void removeTag(Tag tag);

    List<Tag> getTags();

    Tag getTagById(int id);

    Tag getTagByName(String name);
}
