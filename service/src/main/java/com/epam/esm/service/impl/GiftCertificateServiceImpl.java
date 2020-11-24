package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.exception.ValidatorException;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.utils.GiftCertificateQueryGenerator;
import com.epam.esm.utils.ServiceConstant;
import com.epam.esm.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaQuery;
import java.math.BigDecimal;
import java.util.*;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateDAO certificateDAO;
    private TagDAO tagDAO;
    private Validator validator;
    private GiftCertificateQueryGenerator giftCertificateQueryGenerator;
    private GiftCertificateMapper certificateMapper;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDAO certificateDAO,
                                      TagDAO tagDAO,
                                      Validator validator,
                                      GiftCertificateQueryGenerator giftCertificateQueryGenerator,
                                      GiftCertificateMapper certificateMapper) {
        this.certificateDAO = certificateDAO;
        this.tagDAO = tagDAO;
        this.validator = validator;
        this.giftCertificateQueryGenerator = giftCertificateQueryGenerator;
        this.certificateMapper = certificateMapper;
    }

    @Override
    @Transactional
    public GiftCertificateDTO addCertificate(GiftCertificateDTO certificateDTO) {
        return addNewCertificateToDatabase(certificateDTO);
    }

    private GiftCertificateDTO addNewCertificateToDatabase(GiftCertificateDTO certificateDTO) {
        prepareCertificateBeforeAddingToDatabase(certificateDTO);
        validateTags(certificateDTO.getTags());
        return certificateMapper.toDTO(certificateDAO.addCertificate(certificateMapper.toModel(certificateDTO)));
    }

    private void validateTags(Set<TagDTO> tags) {
        tags.forEach(tag -> {
            if (tag.getId() == 0) {
                checkTagNameDoesNotExist(tag);
            } else {
                validator.validateIdIsPositive(tag.getId());
                checkWithSuchIdExists(tag);
            }
        });
    }

    private void checkWithSuchIdExists(TagDTO tag) {
        if (tagDAO.getTagById(tag.getId()) == null)
            throw new ValidatorException(
                    ServiceExceptionCode.NON_EXISTING_TAG_ID.getErrorCode(), String.valueOf(tag.getId()));
    }

    private void checkTagNameDoesNotExist(TagDTO tag) {
        validator.validateTag(tag);
        Tag tagReturned = tagDAO.getTagByName(tag.getName());
        if (tagReturned != null) {
            throw new ValidatorException(
                    ServiceExceptionCode.CANNOT_ADD_EXISTING_TAG.getErrorCode(),
                    tag.getName() + ", id = " + tagReturned.getId());
        }
    }

    private void prepareCertificateBeforeAddingToDatabase(GiftCertificateDTO certificateDTO) {
        certificateDTO.setCreateDate(new Date());
        certificateDTO.setLastUpdateDate(new Date());
        validator.validateCertificate(certificateDTO);
    }

    @Override
    @Transactional
    public void removeCertificate(int certificateId) {
        validator.validateIdIsPositive(certificateId);
        getCertificateIfExists(certificateId);
        certificateDAO.removeCertificate(certificateId);
    }

    @Override
    @Transactional
    public void updateCertificate(int id, GiftCertificateDTO certificateDTO) {
        validator.validateIdIsPositive(id);
        prepareCertificateBeforeUpdatingInDatabase(id, certificateDTO);
        validateTags(certificateDTO.getTags());
        certificateDAO.updateCertificate(certificateMapper.toModel(certificateDTO));
    }

    @Override
    @Transactional
    public void updateCertificateField(int id, Map<String, Object> fields) {
        validator.validateIdIsPositive(id);
        validator.validateCertificateUpdateField(fields);
        GiftCertificateDTO certificate = certificateMapper.toDTO(getCertificateIfExists(id));
        prepareCertificateBeforeUpdatingInDatabase(id, certificate);
        setUpdatedField(certificate, fields);
        certificateDAO.updateCertificate(certificateMapper.toModel(certificate));
    }

    private void setUpdatedField(GiftCertificateDTO certificate, Map<String, Object> fields) {
        fields.forEach((key, value) -> {
            switch (key) {
                case "name":
                    certificate.setName((String) value);
                    break;
                case "description":
                    certificate.setDescription((String) value);
                    break;
                case "price":
                    certificate.setPrice(BigDecimal.valueOf((Double) value));
                    break;
                case "duration":
                    certificate.setDuration((Integer) value);
                    break;
                case "tags":
                    @SuppressWarnings("unchecked cast")
                    List<Map<String, Object>> tags = (ArrayList<Map<String, Object>>) value;
                    Set<TagDTO> tagSet = new HashSet<>();
                    tags.forEach(tagRecord -> {
                        TagDTO tag = new TagDTO();
                        if (tagRecord.get(ServiceConstant.ID_FIELD.getValue()) != null) {
                            tag.setId((Integer) tagRecord.get(ServiceConstant.ID_FIELD.getValue()));
                        }
                        tag.setName((String) tagRecord.get(ServiceConstant.NAME_FIELD.getValue()));
                        tagSet.add(tag);
                    });
                    validateTags(tagSet);
                    certificate.setTags(tagSet);
                    break;
            }
        });
    }

    private void prepareCertificateBeforeUpdatingInDatabase(int id, GiftCertificateDTO certificateDTO) {
        certificateDTO.setLastUpdateDate(new Date());
        certificateDTO.setId(id);
    }

    @Override
    public List<GiftCertificateDTO> getCertificates(Map<String, String> params) {
        validator.validateCertificateParams(params);
        CriteriaQuery<GiftCertificate> criteriaQuery = giftCertificateQueryGenerator.generateQueryCriteria(params);
        List<GiftCertificateDTO> certificates = new ArrayList<>();
        int limit = Integer.parseInt(params.get(ServiceConstant.SIZE_PARAM.getValue()));
        int offset = (Integer.parseInt(params.get(ServiceConstant.PAGE_PARAM.getValue())) - 1) * limit;
        certificateDAO.getCertificates(criteriaQuery, limit, offset).forEach(giftCertificate ->
                certificates.add(certificateMapper.toDTO(giftCertificate)));
        return certificates;
    }

    @Override
    public long getCount(Map<String, String> params) {
        return certificateDAO.getCount(giftCertificateQueryGenerator.generateQueryCriteria(params));
    }

    @Override
    public GiftCertificateDTO getCertificateById(int id) {
        validator.validateIdIsPositive(id);
        return certificateMapper.toDTO(getCertificateIfExists(id));
    }

    private GiftCertificate getCertificateIfExists(int id) {
        GiftCertificate certificate = certificateDAO.getCertificateById(id);
        if (certificate == null) {
            throw new EntityNotFoundException(
                    ServiceExceptionCode.NON_EXISTING_CERTIFICATE_ID.getErrorCode(), String.valueOf(id));
        }
        return certificate;
    }
}
