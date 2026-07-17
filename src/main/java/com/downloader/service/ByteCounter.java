package com.downloader.service;

import org.springframework.stereotype.Service;

@Service
public class ByteCounter {

    private long totalBytes = 0;
    private int completedCount = 0;

    // TODO 1: (Optional) a Lock, or use synchronized.

    public void addBytes(long bytes) {
        // TODO 2: void addBytes(long bytes)
        //         - Add to totalBytes AND increment completedCount.
        //         - MUST be thread-safe (no lost updates when many threads call at once).
    }

    public long getTotalBytes() {
        // TODO 3: long getTotalBytes() -> return safely.
        return totalBytes;
    }

    public int getCompletedCount() {
        // TODO 4: int getCompletedCount() -> return safely.
        return completedCount;
    }
}
