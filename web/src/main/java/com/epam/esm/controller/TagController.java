package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.TagService;
import com.epam.esm.util.HateoasBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller used to manipulate CRD operations on
 * {@code Tag} data
 */
@RestController
@RequestMapping(value = "/api/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {

    private TagService tagService;
    private HateoasBuilder hateoasBuilder;

    @Autowired
    public TagController(TagService tagService,
                         HateoasBuilder hateoasBuilder) {
        this.tagService = tagService;
        this.hateoasBuilder = hateoasBuilder;
    }

    /**
     * Returns list of {@code TagDTO}
     * that represent all tags in the database.
     *
     * @return list of TagDTOs corresponding to tags in the database
     */
    @GetMapping
    @PreAuthorize("permitAll()")
    public RepresentationModel<?> getAllTags(@RequestParam Map<String, String> params) {
        List<TagDTO> tags = tagService.getTags(params);
        long tagsCount = tagService.getCount();
        return hateoasBuilder.addLinksForListOfTagDTOs(tags, params, tagsCount);
    }

    /**
     * Returns {@code TagDTO} requested by id.
     * If no resource found {@code HttpStatus.NOT_FOUND} is returned.
     *
     * @param id id of the requested tag
     * @return TagDTO with the requested id
     */
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public TagDTO getTagById(@PathVariable("id") int id) {
        TagDTO tagDTO = tagService.getTagById(id);
        return hateoasBuilder.addLinksForTagDTO(tagDTO);
    }

    /**
     * Creates a new Tag in the database.
     *
     * @param tag tag to be created
     * @return TagDTO corresponding to Tag that was created
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
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
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<Void> deleteTag(@PathVariable("id") int id) {
        tagService.removeTag(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Special endpoint that returns the most used tag of the user
     * who has spent more money than others on certificates
     *
     * @return TagDTO that represents most used tag of the user with
     * the highest cost of all orders
     */
    @GetMapping("/most-used")
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasAuthority('USER')")
    public TagDTO getMostUsedTagOfUserWithHighestCostOfOrders() {
        return tagService.getMostUsedTagOfUserWithHighestCostOfOrders();
    }
}
