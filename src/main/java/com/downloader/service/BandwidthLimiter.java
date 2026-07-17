package com.downloader.service;

import com.downloader.exception.DownloadException;
import com.downloader.model.DownloadJob;
import org.springframework.stereotype.Service;

import java.util.concurrent.Semaphore;

@Service
public class BandwidthLimiter {

    private final Semaphore semaphore;

    public BandwidthLimiter(int maxParallelDownloads) {
        this.semaphore = new Semaphore(maxParallelDownloads);
    }

    public long download(DownloadJob job) throws DownloadException {
        // TODO 1: public long download(DownloadJob job) throws DownloadException
        //         a) Acquire a permit from the semaphore (blocks if limit reached).
        //         b) try: Thread.sleep(50) to simulate fetching; return job.getSizeInBytes().
        //         c) catch InterruptedException -> throw new DownloadException(...).
        //         d) finally: ALWAYS release the permit.
        return 0L;
    }
}
