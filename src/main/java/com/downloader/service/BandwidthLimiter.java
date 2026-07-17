package com.downloader.service;

import com.downloader.exception.DownloadException;
import com.downloader.model.DownloadJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.Semaphore;

@Service
public class BandwidthLimiter {

    // TODO 1: Final Semaphore field.

    public BandwidthLimiter(@Value("${max.downloads:5}") int maxParallelDownloads) {
        // TODO 2: Constructor(int maxParallelDownloads)
        //         - Initialise the Semaphore with that many permits.
    }

    public long download(DownloadJob job) throws DownloadException {
        // TODO 3: public long download(DownloadJob job) throws DownloadException
        //         a) Acquire a permit (blocks if limit reached).
        //         b) try: Thread.sleep(50) to simulate fetching; return job.getSizeInBytes().
        //         c) catch InterruptedException -> throw new DownloadException(...).
        //         d) finally: ALWAYS release the permit.
        return 0L;
    }
}
