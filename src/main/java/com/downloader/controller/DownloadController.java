package com.downloader.controller;

import com.downloader.model.DownloadJob;
import com.downloader.model.DownloadQueue;
import com.downloader.service.DownloadManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/downloads")
public class DownloadController {

    private final DownloadManager downloadManager;

    public DownloadController(DownloadManager downloadManager) {
        this.downloadManager = downloadManager;
    }

    @PostMapping("/run")
    public long runAll(@RequestBody DownloadQueue<DownloadJob> queue) {
        return downloadManager.runAll(queue);
    }

    @PostMapping("/check-limit")
    public boolean allJobsWithinSizeLimit(@RequestBody DownloadQueue<DownloadJob> queue, @RequestParam long maxBytes) {
        return downloadManager.allJobsWithinSizeLimit(queue, maxBytes);
    }
}
