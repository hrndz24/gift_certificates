package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;

import java.util.List;

public interface TagService {

    void addTag(TagDTO tag);

    void removeTag(int tagId);

    List<TagDTO> getTags();

    TagDTO getTagById(int id);
}
