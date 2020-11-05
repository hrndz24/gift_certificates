package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller used to manipulate CRD operations on
 * {@code Tag} data
 */
@RestController
@RequestMapping(value = "/api/v1/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Returns list of {@code TagDTO}
     * that represent all tags in the database.
     *
     * @return list of TagDTOs corresponding to tags in the database
     */
    @GetMapping
    public List<TagDTO> getAllTags() {
        return tagService.getTags();
    }

    /**
     * Returns {@code TagDTO} requested by id.
     * If no resource found {@code HttpStatus.NOT_FOUND} is returned.
     *
     * @param id id of the requested tag
     * @return TagDTO with the requested id
     */
    @GetMapping("/{id}")
    public TagDTO getTagById(@PathVariable("id") int id) {
        return tagService.getTagById(id);
    }

    /**
     * Creates a new Tag in the database.
     *
     * @param tag tag to be created
     * @return TagDTO corresponding to Tag that was created
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TagDTO createTag(@RequestBody TagDTO tag) {
        return tagService.addTag(tag);
    }

    /**
     * Deletes tag with the specified id.
     *
     * @param id id of the tag to be deleted
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable("id") int id) {
        tagService.removeTag(id);
    }
}
