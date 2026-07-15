package com.downloader.service;

public class ByteCounter {

    private long totalBytes = 0;
    private int completedCount = 0;

    // TODO 1: (Optional) a Lock, or use synchronized.

    // TODO 2: void addBytes(long bytes)
    //         - Add to totalBytes AND increment completedCount.
    //         - MUST be thread-safe (no lost updates when many threads call at once).

    // TODO 3: long getTotalBytes() -> return safely.

    // TODO 4: int getCompletedCount() -> return safely.
}
