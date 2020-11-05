package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateTagDAO;
import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.utils.QueryGenerator;
import com.epam.esm.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateDAO certificateDAO;
    private TagDAO tagDAO;
    private CertificateTagDAO certificateTagDAO;
    private Validator validator;
    private QueryGenerator queryGenerator;
    private GiftCertificateMapper certificateMapper;
    private TagMapper tagMapper;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDAO certificateDAO,
                                      TagDAO tagDAO,
                                      CertificateTagDAO certificateTagDAO,
                                      Validator validator,
                                      QueryGenerator queryGenerator,
                                      GiftCertificateMapper certificateMapper,
                                      TagMapper tagMapper) {
        this.certificateDAO = certificateDAO;
        this.tagDAO = tagDAO;
        this.certificateTagDAO = certificateTagDAO;
        this.validator = validator;
        this.queryGenerator = queryGenerator;
        this.certificateMapper = certificateMapper;
        this.tagMapper = tagMapper;
    }

    @Override
    @Transactional
    public GiftCertificateDTO addCertificate(GiftCertificateDTO certificateDTO) {
        return addNewCertificateToDatabase(certificateDTO);
    }

    private GiftCertificateDTO addNewCertificateToDatabase(GiftCertificateDTO certificateDTO) {
        prepareCertificateBeforeAddingToDatabase(certificateDTO);
        GiftCertificateDTO addedCertificateDTO = certificateMapper.toDTO(
                certificateDAO.addCertificate(certificateMapper.toModel(certificateDTO)));
        addCertificateTags(certificateDTO.getTags(), addedCertificateDTO.getId());
        return addedCertificateDTO;
    }

    private void prepareCertificateBeforeAddingToDatabase(GiftCertificateDTO certificateDTO) {
        certificateDTO.setCreateDate(new Date());
        certificateDTO.setLastUpdateDate(new Date());
        validator.validateCertificate(certificateDTO);
    }

    private void addCertificateTags(Set<TagDTO> tags, int certificateId) {
        tags.forEach(tag -> addTagToCertificateIfExists(certificateId, tag));
    }

    private void addTagToCertificateIfExists(int certificateId, TagDTO tag) {
        int tagId = tag.getId();
        if (!isTagExistent(tag)) {
            tagId = tagMapper.toDTO(tagDAO.addTag(tagMapper.toModel(tag))).getId();
        }
        certificateTagDAO.addTagToCertificate(certificateId, tagId);
    }

    private boolean isTagExistent(TagDTO tagDTO) {
        return tagDAO.getTagById(tagDTO.getId()) != null;
    }

    @Override
    public void removeCertificate(int certificateId) {
        validator.checkIdIsPositive(certificateId);
        getCertificateIfExists(certificateId);
        certificateDAO.removeCertificate(certificateId);
    }

    @Override
    @Transactional
    public void updateCertificate(int id, GiftCertificateDTO certificateDTO) {
        validator.checkIdIsPositive(id);
        if (!isCertificateExistent(id)) {
            addNewCertificateToDatabase(certificateDTO);
        } else {
            prepareCertificateBeforeUpdatingInDatabase(id, certificateDTO);
            certificateDAO.updateCertificate(certificateMapper.toModel(certificateDTO));
        }
    }

    private void prepareCertificateBeforeUpdatingInDatabase(int id, GiftCertificateDTO certificateDTO) {
        certificateDTO.setLastUpdateDate(new Date());
        certificateDTO.setId(id);
        validator.validateCertificate(certificateDTO);
    }

    private boolean isCertificateExistent(int id) {
        return certificateDAO.getCertificateById(id) != null;
    }

    @Override
    public List<GiftCertificateDTO> getCertificates(Map<String, String> params) {
        validator.validateParams(params);
        String query = queryGenerator.generateQuery(params);
        List<GiftCertificateDTO> certificates = new ArrayList<>();
        certificateDAO.getCertificates(query).forEach(giftCertificate ->
                certificates.add(certificateMapper.toDTO(giftCertificate)));
        return certificates;
    }

    @Override
    public GiftCertificateDTO getCertificateById(int id) {
        validator.checkIdIsPositive(id);
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

    @Override
    public void addTagToCertificate(int certificateId, TagDTO tag) {
        validator.checkIdIsPositive(certificateId);
        getCertificateIfExists(certificateId);
        addTagToCertificateIfExists(certificateId, tag);
    }

    @Override
    public void removeTagFromCertificate(int certificateId, int tagId) {
        certificateTagDAO.removeTagFromCertificate(certificateId, tagId);
    }
}
