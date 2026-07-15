package com.downloader.service;

import org.springframework.stereotype.Component;

@Component
public class ByteCounter {

    private long totalBytes = 0;
    private int completedCount = 0;

    public synchronized void addBytes(long bytes) {
        this.totalBytes += bytes;
        this.completedCount++;
    }

    public synchronized long getTotalBytes() {
        return totalBytes;
    }

    public synchronized int getCompletedCount() {
        return completedCount;
    }
}
