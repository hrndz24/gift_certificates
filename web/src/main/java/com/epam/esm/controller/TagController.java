package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/")
    public List<Tag> getAllTags() {
        return tagService.getTags();
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable("id") int id) {
        return tagService.getTagById(id);
    }

    @PostMapping("/")
    public void addTag(@RequestBody Tag tag) {
        tagService.addTag(tag);
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable("id") int id) {
        Tag tag = new Tag();
        tag.setId(id);
        tagService.removeTag(tag);
    }
}
