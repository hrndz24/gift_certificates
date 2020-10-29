package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/certificates", produces = "application/json")
public class GiftCertificateController {

    private GiftCertificateService certificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping("/")
    public void addCertificate(@RequestBody GiftCertificate certificate) {
        certificateService.addCertificate(certificate);
    }

    @PutMapping("/")
    public void updateCertificate(@RequestBody GiftCertificate certificate) {
        certificateService.updateCertificate(certificate);
    }

    @DeleteMapping("/{id}")
    public void deleteCertificate(@PathVariable("id") int id) {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(id);
        certificateService.removeCertificate(certificate);
    }

    @GetMapping("/")
    public List<GiftCertificate> getCertificates() {
        return certificateService.getCertificates();
    }

    @GetMapping("/{id}")
    public GiftCertificate getCertificate(@PathVariable("id") int id) {
        return certificateService.getCertificateById(id);
    }

    @GetMapping("/tagName={name}")
    public List<GiftCertificate> getCertificatesByTagName(@PathVariable("name") String name) {
        return certificateService.getCertificatesByTagName(name);
    }

    @GetMapping("/name={name}")
    public List<GiftCertificate> getCertificatesByName(@PathVariable("name") String name) {
        return certificateService.getCertificatesByName(name);
    }

    @GetMapping("/description={description}")
    public List<GiftCertificate> getCertificatesByDescription(@PathVariable("description") String description) {
        return certificateService.getCertificatesByDescription(description);
    }

    @GetMapping("/sort=date_asc")
    public List<GiftCertificate> getCertificatesSortedByDate() {
        return certificateService.getCertificatesSortedByDateAscending();
    }

    @GetMapping("/sort=date_desc")
    public List<GiftCertificate> getCertificatesSortedByDateDescending() {
        return certificateService.getCertificatesSortedByDateDescending();
    }

    @GetMapping("/sort=name_asc")
    public List<GiftCertificate> getCertificatesSortedByName() {
        return certificateService.getCertificatesSortedByNameAscending();
    }

    @GetMapping("/sort=name_desc")
    public List<GiftCertificate> getCertificatesSortedByNameDescending() {
        return certificateService.getCertificatesSortedByNameDescending();
    }

}
