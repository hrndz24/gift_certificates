package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.TagsDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.exception.ValidatorException;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.utils.ServiceConstant;
import com.epam.esm.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private TagDAO tagDAO;
    private Validator validator;
    private TagMapper mapper;

    @Autowired
    public TagServiceImpl(TagDAO tagDAO,
                          Validator validator,
                          TagMapper mapper) {
        this.tagDAO = tagDAO;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public TagDTO addTag(TagDTO tagDTO) {
        validator.validateTag(tagDTO);
        ensureIdIsNotSet(tagDTO);
        checkTagNameDoesNotExist(tagDTO);
        return mapper.toDTO(tagDAO.addTag(mapper.toModel(tagDTO)));
    }

    private void ensureIdIsNotSet(TagDTO tagDTO) {
        tagDTO.setId(0);
    }

    private void checkTagNameDoesNotExist(TagDTO tag) {
        Tag tagReturned = tagDAO.getTagByName(tag.getName());
        if (tagReturned != null) {
            throw new ValidatorException(
                    ServiceExceptionCode.CANNOT_ADD_EXISTING_TAG.getErrorCode(),
                    tag.getName() + ", id = " + tagReturned.getId());
        }
    }

    @Override
    public void removeTag(int tagId) {
        validator.validateIdIsPositive(tagId);
        Tag tag = getTagIfExists(tagId);
        checkTagIsNotAssignedToAnyCertificate(tag);
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

    private void checkTagIsNotAssignedToAnyCertificate(Tag tag) {
        if (!tag.getCertificates().isEmpty()) {
            throw new ValidatorException(
                    ServiceExceptionCode.CANNOT_DELETE_TAG_WHICH_IS_USED.getErrorCode());
        }
    }

    @Override
    public TagsDTO getTags(Map<String, String> params) {
        List<TagDTO> tags = new ArrayList<>();
        validator.validateTagParams(params);
        int limit = Integer.parseInt(params.get(ServiceConstant.SIZE_PARAM.getValue()));
        int offset = (Integer.parseInt(params.get(ServiceConstant.PAGE_PARAM.getValue())) - 1) * limit;
        long elementsCount = tagDAO.getCount();
        validator.validatePageNumberIsLessThanElementsCount(params, elementsCount);
        tagDAO.getTags(limit, offset).forEach(tag -> tags.add(mapper.toDTO(tag)));
        return new TagsDTO(tags);
    }

    @Override
    public long getCount() {
        return tagDAO.getCount();
    }

    @Override
    public TagDTO getTagById(int id) {
        validator.validateIdIsPositive(id);
        Tag tag = getTagIfExists(id);
        return mapper.toDTO(tag);
    }

    @Override
    public TagDTO getMostUsedTagOfUserWithHighestCostOfOrders() {
        return mapper.toDTO(tagDAO.getMostUsedTagOfUserWithHighestCostOfOrders());
    }
}
