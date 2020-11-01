package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.utils.QueryGenerator;
import com.epam.esm.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateDAO certificateDAO;
    private TagDAO tagDAO;
    private Validator validator;
    private QueryGenerator queryGenerator;
    private GiftCertificateMapper certificateMapper;
    private TagMapper tagMapper;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDAO certificateDAO,
                                      TagDAO tagDAO,
                                      Validator validator,
                                      QueryGenerator queryGenerator,
                                      GiftCertificateMapper certificateMapper,
                                      TagMapper tagMapper) {
        this.certificateDAO = certificateDAO;
        this.tagDAO = tagDAO;
        this.validator = validator;
        this.queryGenerator = queryGenerator;
        this.certificateMapper = certificateMapper;
        this.tagMapper = tagMapper;
    }

    @Override
    public GiftCertificateDTO addCertificate(GiftCertificateDTO certificateDTO) {
        prepareCertificateBeforeAddingToDatabase(certificateDTO);
        GiftCertificateDTO addedCertificateDTO = certificateMapper.toDTO(
                certificateDAO.addCertificate(certificateMapper.toModel(certificateDTO)));
        addCertificateTags(certificateDTO.getTags(), addedCertificateDTO.getId());
        return addedCertificateDTO;
    }

    private void prepareCertificateBeforeAddingToDatabase(GiftCertificateDTO certificateDTO) {
        validator.checkNonNull(certificateDTO, certificateDTO.getClass().getName());
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
        certificateDAO.addTagToCertificate(certificateId, tagId);
    }

    private boolean isTagExistent(TagDTO tagDTO) {
        try {
            tagDAO.getTagById(tagDTO.getId());
        } catch (DAOException e) {
            return false;
        }
        return true;
    }

    @Override
    public void removeCertificate(int certificateId) {
        certificateDAO.removeCertificate(certificateId);
    }

    @Override
    public void updateCertificate(int id, GiftCertificateDTO certificateDTO) {
        validator.validateCertificate(certificateDTO);
        certificateDTO.setLastUpdateDate(new Date());
        certificateDTO.setId(id);
        certificateDAO.updateCertificate(certificateMapper.toModel(certificateDTO));
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
        return certificateMapper.toDTO(certificateDAO.getCertificateById(id));
    }

    @Override
    public void addTagToCertificate(int certificateId, TagDTO tag) {
        addTagToCertificateIfExists(certificateId, tag);
    }

    @Override
    public void removeTagFromCertificate(int certificateId, int tagId) {
        certificateDAO.removeTagFromCertificate(certificateId, tagId);
    }
}
