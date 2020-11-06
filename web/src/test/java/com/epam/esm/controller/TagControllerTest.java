package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class TagControllerTest {

    private TagService service;

    private MockMvc mockMvc;

    private static final String tagsURL = "/api/v1/tags";

    @BeforeEach
    void setUp() {
        service = Mockito.mock(TagService.class);
        TagController controller = new TagController(service);
        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    void getAllTags() throws Exception {
        List<TagDTO> tags = new ArrayList<>();
        tags.add(new TagDTO());
        tags.add(new TagDTO());
        tags.add(new TagDTO());
        given(service.getTags()).willReturn(tags);
        mockMvc.perform(get(tagsURL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(tags.size()));
    }

    @Test
    void getTagById() throws Exception {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(2);
        tagDTO.setName("geek");
        given(service.getTagById(2)).willReturn(tagDTO);
        mockMvc.perform(get(tagsURL + "/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("geek"));
    }

    @Test
    void createTag() throws Exception {
        TagDTO tagReturned = new TagDTO();
        tagReturned.setId(1);
        tagReturned.setName("newTag");
        String requestBody = "{" +
                "\"name\":\"newTag\"" +
                "}";
        given(service.addTag(any())).willReturn(tagReturned);
        mockMvc.perform(
                post(tagsURL)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("newTag"));
    }

    @Test
    void deleteTag() throws Exception {
        int id = 1;
        willDoNothing().given(service).removeTag(id);
        mockMvc.perform(delete(tagsURL + "/{id}", id))
                .andExpect(status().isNoContent());
    }
}