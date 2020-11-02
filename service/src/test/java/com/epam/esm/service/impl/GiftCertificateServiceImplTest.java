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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GiftCertificateServiceImplTest {

    @InjectMocks
    private GiftCertificateServiceImpl certificateService;
    @Mock
    private GiftCertificateDAO certificateDAO;
    @Mock
    private TagDAO tagDAO;
    @Mock
    private Validator validator;
    @Mock
    private QueryGenerator queryGenerator;
    @Mock
    private GiftCertificateMapper certificateMapper;
    @Mock
    private TagMapper tagMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        when(certificateDAO.getCertificateById(1)).thenReturn(new GiftCertificate());
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
        tag.setId(1);
        when(tagDAO.getTagById(1)).thenReturn(new Tag());
        when(certificateDAO.getCertificateById(2)).thenReturn(new GiftCertificate());
        doNothing().when(certificateDAO).addTagToCertificate(2, 1);
        certificateService.addTagToCertificate(2, tag);
    }

    @Test
    void removeTagFromCertificate() {
        doNothing().when(certificateDAO).removeTagFromCertificate(3, 2);
        certificateService.removeTagFromCertificate(3, 2);
    }
}