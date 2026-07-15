package com.downloader.service;

import org.junit.jupiter.api.Test;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ByteCounterTest {

    @Test
    void testThreadSafety() throws InterruptedException {
        ByteCounter counter = new ByteCounter();
        int threads = 100;
        int additionsPerThread = 1000;
        long bytesToAdd = 10;
        
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < additionsPerThread; j++) {
                    counter.addBytes(bytesToAdd);
                }
                latch.countDown();
            });
        }

        latch.await();
        executorService.shutdown();

        assertEquals(threads * additionsPerThread * bytesToAdd, counter.getTotalBytes(), "Total bytes mismatch, race condition detected!");
        assertEquals(threads * additionsPerThread, counter.getCompletedCount(), "Completed count mismatch, race condition detected!");
    }
}
