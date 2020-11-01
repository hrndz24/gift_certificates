package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ValidatorException;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.utils.QueryGenerator;
import com.epam.esm.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GiftCertificateServiceImplTest {

    private GiftCertificateServiceImpl certificateService;
    private GiftCertificateDAO certificateDAO;
    private TagDAO tagDAO;
    private Validator validator;
    private QueryGenerator queryGenerator;
    private GiftCertificateMapper certificateMapper;
    private TagMapper tagMapper;

    @BeforeEach
    void setUp() {
        certificateDAO = mock(GiftCertificateDAO.class);
        tagDAO = mock(TagDAO.class);
        validator = mock(Validator.class);
        queryGenerator = mock(QueryGenerator.class);
        certificateMapper = mock(GiftCertificateMapper.class);
        tagMapper = mock(TagMapper.class);
        certificateService = new GiftCertificateServiceImpl(certificateDAO,
                tagDAO, validator, queryGenerator, certificateMapper, tagMapper);
    }

    @Test
    void addCertificate() {
        GiftCertificate certificateToAdd = new GiftCertificate();
        GiftCertificate certificateReturned = new GiftCertificate();
        certificateReturned.setId(24);
        when(certificateDAO.addCertificate(certificateToAdd)).thenReturn(certificateReturned);
        GiftCertificateDTO certificateDTOToAdd = new GiftCertificateDTO();
        doNothing().when(validator).validateCertificate(certificateDTOToAdd);
        GiftCertificateDTO certificateDTOReturned = new GiftCertificateDTO();
        certificateDTOReturned.setId(24);
        when(certificateMapper.toModel(certificateDTOToAdd)).thenReturn(certificateToAdd);
        when(certificateMapper.toDTO(certificateReturned)).thenReturn(certificateDTOReturned);
        assertEquals(24, certificateService.addCertificate(certificateDTOToAdd).getId());
        assertNotNull(certificateDTOToAdd.getCreateDate());
        assertNotNull(certificateDTOToAdd.getLastUpdateDate());
    }

    @Test
    void addCertificate_InvalidData() {
        GiftCertificateDTO certificateDTOToAdd = new GiftCertificateDTO();
        doThrow(ValidatorException.class).when(validator).validateCertificate(certificateDTOToAdd);
        assertThrows(ValidatorException.class, () -> certificateService.addCertificate(certificateDTOToAdd));
    }

    @Test
    void removeCertificate() {
        doNothing().when(certificateDAO).removeCertificate(1);
        certificateService.removeCertificate(1);
    }

    @Test
    void updateCertificate() {
        GiftCertificate certificateToUpdate = new GiftCertificate();
        GiftCertificateDTO certificateDTOToUpdate = new GiftCertificateDTO();
        doNothing().when(validator).validateCertificate(certificateDTOToUpdate);
        when(certificateMapper.toModel(certificateDTOToUpdate)).thenReturn(certificateToUpdate);
        doNothing().when(certificateDAO).updateCertificate(certificateToUpdate);
        when(certificateDAO.getCertificateById(2)).thenReturn(new GiftCertificate());
        certificateService.updateCertificate(2, certificateDTOToUpdate);
        assertEquals(2, certificateDTOToUpdate.getId());
        assertNotNull(certificateDTOToUpdate.getLastUpdateDate());
    }

    @Test
    void updateCertificate_NonExistingId() {
        GiftCertificateDTO certificateDTOToUpdate = new GiftCertificateDTO();
        when(certificateDAO.getCertificateById(2)).thenReturn(null);
        assertThrows(EntityNotFoundException.class,
                () -> certificateService.updateCertificate(2, certificateDTOToUpdate));
    }

    @Test
    void getCertificates() {
    }

    @Test
    void getCertificateById() {
        GiftCertificate certificateReturned = new GiftCertificate();
        certificateReturned.setId(2);
        when(certificateDAO.getCertificateById(2)).thenReturn(certificateReturned);
        GiftCertificateDTO certificateDTOReturned = new GiftCertificateDTO();
        certificateDTOReturned.setId(2);
        certificateDTOReturned.setName("certificate");
        when(certificateMapper.toDTO(certificateReturned)).thenReturn(certificateDTOReturned);
        assertEquals(certificateDTOReturned, certificateService.getCertificateById(2));
    }

    @Test
    void getCertificateById_NonExistingId() {
        when(certificateDAO.getCertificateById(24)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> certificateService.getCertificateById(24));
    }

    @Test
    void addTagToCertificate() {
        TagDTO tag = new TagDTO();
        tag.setId(2);
        when(tagDAO.getTagById(2)).thenReturn(new Tag());
        doNothing().when(certificateDAO).addTagToCertificate(3, 2);
        certificateService.addTagToCertificate(3, tag);
    }

    @Test
    void removeTagFromCertificate() {
        doNothing().when(certificateDAO).removeTagFromCertificate(3, 2);
        certificateService.removeTagFromCertificate(3, 2);
    }
}