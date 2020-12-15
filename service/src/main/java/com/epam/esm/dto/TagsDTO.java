package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

public class TagsDTO extends RepresentationModel<TagsDTO> {

    private List<TagDTO> tags;

    public TagsDTO() {
    }

    public TagsDTO(List<TagDTO> tags) {
        this.tags = tags;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TagsDTO tagsDTO = (TagsDTO) o;
        return Objects.equals(tags, tagsDTO.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tags);
    }

    @Override
    public String toString() {
        return "TagsDTO{" +
                "tags=" + tags +
                '}';
    }
}
