package com.epam.esm.mapper;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.model.GiftCertificate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GiftCertificateMapper {

    private ModelMapper mapper;

    @Autowired
    public GiftCertificateMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public GiftCertificate toModel(GiftCertificateDTO dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, GiftCertificate.class);
    }

    public GiftCertificateDTO toDTO(GiftCertificate model) {
        return Objects.isNull(model) ? null : mapper.map(model, GiftCertificateDTO.class);
    }
}
