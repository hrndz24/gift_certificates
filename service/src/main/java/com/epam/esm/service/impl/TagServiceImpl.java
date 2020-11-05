package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateTagDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.exception.ValidatorException;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagServiceImpl implements TagService {

    private TagDAO tagDAO;
    private CertificateTagDAO certificateTagDAO;
    private Validator validator;
    private TagMapper mapper;

    @Autowired
    public TagServiceImpl(TagDAO tagDAO,
                          CertificateTagDAO certificateTagDAO,
                          Validator validator,
                          TagMapper mapper) {
        this.tagDAO = tagDAO;
        this.certificateTagDAO = certificateTagDAO;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public TagDTO addTag(TagDTO tagDTO) {
        validator.validateTag(tagDTO);
        Tag tag = addTagIfItDoesNotExist(mapper.toModel(tagDTO));
        return mapper.toDTO(tag);
    }

    private Tag addTagIfItDoesNotExist(Tag tag) {
        try {
            return tagDAO.addTag(tag);
        } catch (DAOException e) {
            throw new ValidatorException(
                    ServiceExceptionCode.CANNOT_ADD_EXISTING_TAG.getErrorCode());
        }
    }

    @Override
    public void removeTag(int tagId) {
        validator.checkIdIsPositive(tagId);
        getTagIfExists(tagId);
        checkTagIsNotAssignedToAnyCertificate(tagId);
        tagDAO.removeTag(tagId);
    }

    private Tag getTagIfExists(int tagId) {
        Tag tag = tagDAO.getTagById(tagId);
        if (tag == null) {
            throw new EntityNotFoundException(
                    ServiceExceptionCode.NON_EXISTING_TAG_ID.getErrorCode(), String.valueOf(tagId));
        }
        return tag;
    }

    private void checkTagIsNotAssignedToAnyCertificate(int tagId) {
        if (certificateTagDAO.isTagAssignedToAnyCertificate(tagId)) {
            throw new ValidatorException(
                    ServiceExceptionCode.CANNOT_DELETE_TAG_WHICH_IS_USED.getErrorCode());
        }
    }

    @Override
    public List<TagDTO> getTags() {
        List<TagDTO> tags = new ArrayList<>();
        tagDAO.getTags().forEach(tag -> tags.add(mapper.toDTO(tag)));
        return tags;
    }

    @Override
    public TagDTO getTagById(int id) {
        validator.checkIdIsPositive(id);
        Tag tag = getTagIfExists(id);
        return mapper.toDTO(tag);
    }
}
