package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.TagService;
import com.epam.esm.util.ControllerPaginationPreparer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller used to manipulate CRD operations on
 * {@code Tag} data
 */
@RestController
@RequestMapping(value = "/api/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {

    private TagService tagService;
    private ControllerPaginationPreparer paginationPreparer;

    @Autowired
    public TagController(TagService tagService,
                         ControllerPaginationPreparer paginationPreparer) {
        this.tagService = tagService;
        this.paginationPreparer = paginationPreparer;
    }

    /**
     * Returns list of {@code TagDTO}
     * that represent all tags in the database.
     *
     * @return list of TagDTOs corresponding to tags in the database
     */
    @GetMapping
    public RepresentationModel<?> getAllTags(@RequestParam Map<String, String> params) {
        List<TagDTO> tags = tagService.getTags(params);
        tags.forEach(tagDTO -> {
            tagDTO.add(linkTo(methodOn(TagController.class)
                    .getTagById(tagDTO.getId()))
                    .withSelfRel());
        });
        int currentPage = Integer.parseInt(params.get("page"));
        long tagsCount = tagService.getCount();
        List<Link> links = paginationPreparer.prepareLinks(
                methodOn(TagController.class).getAllTags(params), params, currentPage, tagsCount);
        Map<String, Long> page = paginationPreparer.preparePageInfo(params, currentPage, tagsCount);
        CollectionModel<TagDTO> collectionModel = CollectionModel.of(tags);
        return HalModelBuilder.halModelOf(collectionModel).links(links).embed(page, LinkRelation.of("page")).build();
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
        TagDTO tagDTO = tagService.getTagById(id);
        tagDTO.add(linkTo(methodOn(TagController.class)
                .getTagById(tagDTO.getId()))
                .withSelfRel());
        tagDTO.add(linkTo(methodOn(TagController.class)
                .deleteTag(tagDTO.getId()))
                .withRel("delete"));
        Map<String, String> params = new HashMap<>();
        params.put("tagName", tagDTO.getName());
        tagDTO.add(linkTo(methodOn(GiftCertificateController.class)
                .getCertificates(params))
                .withRel("certificates"));
        return tagDTO;
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
        TagDTO tagDTO = tagService.addTag(tag);
        tagDTO.add(linkTo(methodOn(TagController.class)
                .createTag(tag))
                .withSelfRel());
        tagDTO.add(linkTo(methodOn(TagController.class)
                .getTagById(tagDTO.getId()))
                .withRel("get"));
        tagDTO.add(linkTo(methodOn(TagController.class)
                .deleteTag(tagDTO.getId()))
                .withRel("delete"));
        return tagDTO;
    }

    /**
     * Deletes tag with the specified id.
     *
     * @param id id of the tag to be deleted
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTag(@PathVariable("id") int id) {
        tagService.removeTag(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
