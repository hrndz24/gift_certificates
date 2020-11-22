package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;

import java.util.List;
import java.util.Map;

public interface TagService {

    TagDTO addTag(TagDTO tag);

    void removeTag(int tagId);

    List<TagDTO> getTags(Map<String, String> params);

    TagDTO getTagById(int id);

    long getCount();
}
