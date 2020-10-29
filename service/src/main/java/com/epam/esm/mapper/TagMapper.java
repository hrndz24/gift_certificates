package com.epam.esm.mapper;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.model.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TagMapper {

    private ModelMapper mapper;

    @Autowired
    public TagMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Tag toModel(TagDTO dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Tag.class);
    }

    public TagDTO toDTO(Tag model) {
        return Objects.isNull(model) ? null : mapper.map(model, TagDTO.class);
    }
}
