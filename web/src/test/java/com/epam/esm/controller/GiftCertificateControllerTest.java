package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.GiftCertificateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class GiftCertificateControllerTest {

    private GiftCertificateService service;

    private MockMvc mockMvc;

    private static final String certificatesURL = "/api/v1/certificates";

    @BeforeEach
    void setUp() {
        service = Mockito.mock(GiftCertificateService.class);
        GiftCertificateController controller = new GiftCertificateController(service);
        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    void createCertificate() throws Exception {
        GiftCertificateDTO certificateReturned = new GiftCertificateDTO();
        certificateReturned.setId(1);
        certificateReturned.setName("newCertificate");
        certificateReturned.setDescription("Nice description");
        certificateReturned.setPrice(new BigDecimal("14.00"));
        Date date = new Date();
        certificateReturned.setCreateDate(date);
        certificateReturned.setLastUpdateDate(date);
        certificateReturned.setDuration(23);
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        String dateAsISO = df.format(date);
        String requestBody = "{" +
                "\"name\":\"newCertificate\"," +
                "\"description\":\"Nice description\"," +
                "\"price\": 14.00," +
                "\"duration\": 23" +
                "}";
        given(service.addCertificate(any())).willReturn(certificateReturned);
        mockMvc.perform(
                post(certificatesURL).content(requestBody).
                        contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("newCertificate"))
                .andExpect(jsonPath("$.description").value("Nice description"))
                .andExpect(jsonPath("$.price").value(14.00))
                .andExpect(jsonPath("$.createDate").value(dateAsISO))
                .andExpect(jsonPath("$.lastUpdateDate").value(dateAsISO))
                .andExpect(jsonPath("$.duration").value(23));
    }

    @Test
    void updateCertificate() throws Exception {
        String requestBody = "{" +
                "\"name\":\"newCertificate\"," +
                "\"description\":\"Nice description\"," +
                "\"price\": 14.00," +
                "\"duration\": 23" +
                "}";
        willDoNothing().given(service).updateCertificate(anyInt(), any());
        mockMvc.perform(
                put(certificatesURL + "/{id}", 1)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCertificate() throws Exception {
        int id = 1;
        willDoNothing().given(service).removeCertificate(id);
        mockMvc.perform(delete(certificatesURL + "/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void getCertificates() throws Exception {
        List<GiftCertificateDTO> certificates = new ArrayList<>();
        certificates.add(new GiftCertificateDTO());
        certificates.add(new GiftCertificateDTO());
        certificates.add(new GiftCertificateDTO());
        given(service.getCertificates(new HashMap<>())).willReturn(certificates);
        mockMvc.perform(get(certificatesURL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(certificates.size()));
    }

    @Test
    void getCertificates_WithParameters() throws Exception {
        List<GiftCertificateDTO> certificates = new ArrayList<>();
        GiftCertificateDTO certificateReturned = new GiftCertificateDTO();
        certificateReturned.setId(1);
        certificateReturned.setName("Disney");
        certificates.add(certificateReturned);
        given(service.getCertificates(anyMap())).willReturn(certificates);
        String requestBody = "{" +
                "\"certificateName\":\"Dis\"" +
                "}";
        mockMvc.perform(
                get(certificatesURL)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("Disney"));
    }

    @Test
    void getCertificateById() throws Exception {
        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setId(1);
        certificateDTO.setName("Disney");
        given(service.getCertificateById(1)).willReturn(certificateDTO);
        mockMvc.perform(get(certificatesURL + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Disney"));
    }

    @Test
    void addTagToCertificate() throws Exception {
        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setId(1);
        Set<TagDTO> tags = new HashSet<>();
        TagDTO tag = new TagDTO();
        tag.setId(1);
        tag.setName("sport");
        tags.add(tag);
        certificateDTO.setTags(tags);
        given(service.getCertificateById(1)).willReturn(certificateDTO);
        willDoNothing().given(service).addTagToCertificate(1, tag);
        String requestBody = "{" +
                "\"id\": 1," +
                "\"name\":\"sport\"" +
                "}";
        mockMvc.perform(
                post(certificatesURL + "/{id}/tags", 1)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tags.[0].id").value(1))
                .andExpect(jsonPath("$.tags.[0].name").value("sport"));
    }

    @Test
    void deleteTagFromCertificate() throws Exception {
        int certificateId = 1;
        int tagId = 1;
        willDoNothing().given(service).removeTagFromCertificate(certificateId, tagId);
        mockMvc.perform(delete(certificatesURL + "/{certificateId}/tags/{tagId}", certificateId, tagId))
                .andExpect(status().isOk());
    }
}