package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.utils.GiftCertificateQueryGenerator;
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
    private Validator validator;
    private GiftCertificateQueryGenerator giftCertificateQueryGenerator;
    private GiftCertificateMapper certificateMapper;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDAO certificateDAO,
                                      Validator validator,
                                      GiftCertificateQueryGenerator giftCertificateQueryGenerator,
                                      GiftCertificateMapper certificateMapper) {
        this.certificateDAO = certificateDAO;
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
        return certificateMapper.toDTO(certificateDAO.addCertificate(certificateMapper.toModel(certificateDTO)));
    }

    private void prepareCertificateBeforeAddingToDatabase(GiftCertificateDTO certificateDTO) {
        certificateDTO.setCreateDate(new Date());
        certificateDTO.setLastUpdateDate(new Date());
        validator.validateCertificate(certificateDTO);
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
        prepareCertificateBeforeUpdatingInDatabase(id, certificateDTO);
        certificateDAO.updateCertificate(certificateMapper.toModel(certificateDTO));
    }

    @Override
    public void updateCertificateField(int id, Map<String, Object> fields) {
        validator.checkIdIsPositive(id);
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
                    certificate.setPrice((BigDecimal) value);
                    break;
                case "duration":
                    certificate.setDuration((Integer) value);
                    break;
                case "tags":
                    //check tags
                    @SuppressWarnings("unchecked cast")
                    List<TagDTO> tags = (ArrayList<TagDTO>) value;
                    Set<TagDTO> tagSet = new HashSet<>(tags);
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
        certificateDAO.getCertificates(criteriaQuery).forEach(giftCertificate ->
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
}
