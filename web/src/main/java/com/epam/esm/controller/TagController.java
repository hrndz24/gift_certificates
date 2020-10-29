package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/tags", produces = "application/json")
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/")
    public List<TagDTO> getAllTags() {
        return tagService.getTags();
    }

    @GetMapping("/{id}")
    public TagDTO getTagById(@PathVariable("id") int id) {
        return tagService.getTagById(id);
    }

    @PostMapping("/")
    public void createTag(@RequestBody TagDTO tag) {
        tagService.addTag(tag);
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable("id") int id) {
        TagDTO tag = new TagDTO();
        tag.setId(id);
        tagService.removeTag(tag);
    }
}
