package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.GiftCertificatesDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.specification.Specification;
import com.epam.esm.utils.GiftCertificateQueryGenerator;
import com.epam.esm.utils.ServiceConstant;
import com.epam.esm.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateDAO certificateDAO;
    private TagDAO tagDAO;
    private Validator validator;
    private GiftCertificateQueryGenerator giftCertificateQueryGenerator;
    private GiftCertificateMapper certificateMapper;

    private static final String NAME_FIELD = "name";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String TAGS_FIELD = "tags";
    private static final String DURATION_FIELD = "duration";
    private static final String PRICE_FIELD = "price";

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
            validator.validateTag(tag);
            Tag tagReturned = tagDAO.getTagByName(tag.getName());
            if (tagReturned != null) {
                tag.setId(tagReturned.getId());
            } else {
                withdrawIdIfSet(tag);
            }
        });
    }

    private void withdrawIdIfSet(TagDTO tag) {
        tag.setId(0);
    }

    private void prepareCertificateBeforeAddingToDatabase(GiftCertificateDTO certificateDTO) {
        certificateDTO.setCreateDate(new Date());
        certificateDTO.setLastUpdateDate(new Date());
        validator.validateCertificate(certificateDTO);
    }

    @Override
    public void removeCertificate(int certificateId) {
        validator.validateIdIsPositive(certificateId);
        getCertificateIfExists(certificateId);
        certificateDAO.removeCertificate(certificateId);
    }

    @Override
    public void updateCertificate(int id, GiftCertificateDTO certificateDTO) {
        validator.validateIdIsPositive(id);
        getCertificateIfExists(id);
        validator.validateCertificate(certificateDTO);
        prepareCertificateBeforeUpdatingInDatabase(id, certificateDTO);
        validateTags(certificateDTO.getTags());
        certificateDAO.updateCertificate(certificateMapper.toModel(certificateDTO));
    }

    @Override
    public void updateCertificateField(int id, Map<String, Object> fields) {
        validator.validateIdIsPositive(id);
        getCertificateIfExists(id);
        validator.validateCertificateUpdateField(fields);
        GiftCertificateDTO certificate = certificateMapper.toDTO(getCertificateIfExists(id));
        prepareCertificateBeforeUpdatingInDatabase(id, certificate);
        setUpdatedField(certificate, fields);
        certificateDAO.updateCertificate(certificateMapper.toModel(certificate));
    }

    private void setUpdatedField(GiftCertificateDTO certificate, Map<String, Object> fields) {
        fields.forEach((key, value) -> {
            switch (key) {
                case NAME_FIELD:
                    certificate.setName((String) value);
                    break;
                case DESCRIPTION_FIELD:
                    certificate.setDescription((String) value);
                    break;
                case PRICE_FIELD:
                    certificate.setPrice(BigDecimal.valueOf((Double) value));
                    break;
                case DURATION_FIELD:
                    certificate.setDuration((Integer) value);
                    break;
                case TAGS_FIELD:
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
    public GiftCertificatesDTO getCertificates(Map<String, String> params) {
        validator.validateCertificateParams(params);
        List<Specification> specifications = giftCertificateQueryGenerator.generateQueryCriteria(params);
        long elementsCount = certificateDAO.getCount(specifications);
        validator.validatePageNumberIsLessThanElementsCount(params, elementsCount);
        List<GiftCertificateDTO> certificates = new ArrayList<>();
        int limit = Integer.parseInt(params.get(ServiceConstant.SIZE_PARAM.getValue()));
        int offset = (Integer.parseInt(params.get(ServiceConstant.PAGE_PARAM.getValue())) - 1) * limit;
        certificateDAO.getCertificates(specifications, limit, offset).forEach(giftCertificate ->
                certificates.add(certificateMapper.toDTO(giftCertificate)));
        return new GiftCertificatesDTO(certificates);
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
