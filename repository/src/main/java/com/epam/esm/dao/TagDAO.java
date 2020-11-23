package com.epam.esm.dao;

import com.epam.esm.model.Tag;

import java.util.List;

public interface TagDAO {

    Tag addTag(Tag tag);

    void removeTag(int tagId);

    List<Tag> getTags(int limit, int offset);

    Tag getTagById(int id);

    Tag getTagByName(String name);

    long getCount();

    Tag getMostUsedTagOfUserWithHighestCostOfOrders();
}
