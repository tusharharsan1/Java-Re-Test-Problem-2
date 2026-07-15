package com.downloader.controller;

import com.downloader.model.DownloadJob;
import com.downloader.model.DownloadQueue;
import com.downloader.service.DownloadManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DownloadController.class)
class DownloadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DownloadManager downloadManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRunAll() throws Exception {
        DownloadJob job = new DownloadJob("http://example.com", 100);
        DownloadQueue<DownloadJob> queue = new DownloadQueue<>(Arrays.asList(job));

        when(downloadManager.runAll(any())).thenReturn(100L);

        mockMvc.perform(post("/api/downloads/run")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(queue)))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));
    }

    @Test
    void testAllJobsWithinSizeLimit() throws Exception {
        DownloadJob job = new DownloadJob("http://example.com", 100);
        DownloadQueue<DownloadJob> queue = new DownloadQueue<>(Arrays.asList(job));

        when(downloadManager.allJobsWithinSizeLimit(any(), eq(500L))).thenReturn(true);

        mockMvc.perform(post("/api/downloads/check-limit")
                .param("maxBytes", "500")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(queue)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}