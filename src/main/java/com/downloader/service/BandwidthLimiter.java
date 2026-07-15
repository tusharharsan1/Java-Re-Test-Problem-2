package com.downloader.service;

import com.downloader.exception.DownloadException;
import com.downloader.model.DownloadJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;

@Component
public class BandwidthLimiter {

    private final Semaphore semaphore;

    public BandwidthLimiter(@Value("${max.parallel.downloads:2}") int maxParallelDownloads) {
        this.semaphore = new Semaphore(maxParallelDownloads);
    }

    public long download(DownloadJob job) throws DownloadException {
        try {
            semaphore.acquire();
            Thread.sleep(50);
            return job.getSizeInBytes();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DownloadException("Download interrupted for job: " + job.getJobId());
        } finally {
            semaphore.release();
        }
    }
}
