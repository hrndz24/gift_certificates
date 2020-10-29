package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public List<GiftCertificate> getCertificates(@RequestParam Map<String, String> params) {
        return certificateService.getCertificates(params);
    }

    @GetMapping("/{id}")
    public GiftCertificate getCertificate(@PathVariable("id") int id) {
        return certificateService.getCertificateById(id);
    }
}
