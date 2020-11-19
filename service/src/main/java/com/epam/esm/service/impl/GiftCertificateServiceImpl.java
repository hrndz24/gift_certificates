package com.epam.esm.service.impl;

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
import com.epam.esm.utils.GiftCertificateQueryGenerator;
import com.epam.esm.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateDAO certificateDAO;
    private TagDAO tagDAO;
    private Validator validator;
    private GiftCertificateQueryGenerator giftCertificateQueryGenerator;
    private GiftCertificateMapper certificateMapper;
    private TagMapper tagMapper;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDAO certificateDAO,
                                      TagDAO tagDAO,
                                      Validator validator,
                                      GiftCertificateQueryGenerator giftCertificateQueryGenerator,
                                      GiftCertificateMapper certificateMapper,
                                      TagMapper tagMapper) {
        this.certificateDAO = certificateDAO;
        this.tagDAO = tagDAO;
        this.validator = validator;
        this.giftCertificateQueryGenerator = giftCertificateQueryGenerator;
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
        setUpdatedCertificateValues(certificate, fields);
        prepareCertificateBeforeUpdatingInDatabase(id, certificate);
        certificateDAO.updateCertificate(certificateMapper.toModel(certificate));
    }

    private void setUpdatedCertificateValues(GiftCertificateDTO certificate, Map<String, Object> fields) {
        fields.entrySet().forEach((entry) -> {
            if (entry.getKey().equals("name")) {
                validator.validateCertificateUpdateFieldMatchesDataType(String.class, entry);
                certificate.setName((String) entry.getValue());
            }
            if (entry.getKey().equals("description")) {
                validator.validateCertificateUpdateFieldMatchesDataType(String.class, entry);
                certificate.setDescription((String) entry.getValue());
            }
            if (entry.getKey().equals("price")) {
                validator.validateCertificateUpdateFieldMatchesDataType(Double.class, entry);
                certificate.setPrice(BigDecimal.valueOf((Double) entry.getValue()));
            }
            if (entry.getKey().equals("duration")) {
                validator.validateCertificateUpdateFieldMatchesDataType(Integer.class, entry);
                certificate.setDuration((Integer) entry.getValue());
            }
            if (entry.getKey().equals("tags")) {
                validator.validateCertificateUpdateFieldMatchesDataType(ArrayList.class, entry);
                List<?> tagList = (ArrayList<?>) entry.getValue();
                Set<TagDTO> tags = new HashSet<>();
                tagList.forEach(tagRecord -> {
                    TagDTO tag = new TagDTO();
                    Map<?, ?> tagFields = (Map<?, ?>) tagRecord;
                    Object id = tagFields.get("id");
                    if (id == null) {
                        tag.setId(0);
                    } else {
                        tag.setId((Integer) id);
                    }
                    tag.setName((String) tagFields.get("name"));
                    tags.add(tag);
                });
//                updateCertificateTags(certificate.getId(), tags);
            }
        });
    }

    private void prepareCertificateBeforeUpdatingInDatabase(int id, GiftCertificateDTO certificateDTO) {
        certificateDTO.setLastUpdateDate(new Date());
        certificateDTO.setId(id);
        validator.validateCertificate(certificateDTO);
    }

    @Override
    public List<GiftCertificateDTO> getCertificates(Map<String, String> params) {
        validator.validateCertificateParams(params);
        String query = giftCertificateQueryGenerator.generateQuery(params);
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
}
