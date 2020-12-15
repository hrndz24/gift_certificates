package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.TagsDTO;

import java.util.Map;

public interface TagService {

    TagDTO addTag(TagDTO tag);

    void removeTag(int tagId);

    TagsDTO getTags(Map<String, String> params);

    TagDTO getTagById(int id);

    long getCount();

    TagDTO getMostUsedTagOfUserWithHighestCostOfOrders();
}
