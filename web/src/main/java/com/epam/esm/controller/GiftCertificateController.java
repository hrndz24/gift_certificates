package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/certificates", produces = "application/json")
public class GiftCertificateController {

    private GiftCertificateService certificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDTO createCertificate(@RequestBody GiftCertificateDTO certificate) {
        return certificateService.addCertificate(certificate);
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateCertificate(@PathVariable int id, @RequestBody GiftCertificateDTO certificate) {
        certificateService.updateCertificate(id, certificate);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCertificate(@PathVariable("id") int id) {
        certificateService.removeCertificate(id);
    }

    @GetMapping
    public List<GiftCertificateDTO> getCertificates(@RequestParam Map<String, String> params) {
        return certificateService.getCertificates(params);
    }

    @GetMapping("{id}")
    public GiftCertificateDTO getCertificate(@PathVariable("id") int id) {
        return certificateService.getCertificateById(id);
    }

    @PostMapping(value = "/{id}/tags", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDTO addTagToCertificate(@PathVariable("id") int id, @RequestBody TagDTO tag) {
        certificateService.addTagToCertificate(id, tag);
        return certificateService.getCertificateById(id);
    }

    @DeleteMapping("{certificateId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTagFromCertificate(@PathVariable("certificateId") int certificateId,
                                         @PathVariable("tagId") int tagId) {
        certificateService.removeTagFromCertificate(certificateId, tagId);
    }
}
