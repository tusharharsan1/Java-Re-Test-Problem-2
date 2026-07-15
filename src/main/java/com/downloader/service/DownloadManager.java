package com.downloader.service;

import com.downloader.exception.DownloadException;
import com.downloader.exception.InvalidJobException;
import com.downloader.exception.MalformedUrlCustomException;
import com.downloader.model.DownloadJob;
import com.downloader.model.DownloadQueue;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class DownloadManager {

    private final BandwidthLimiter limiter;
    private final ByteCounter counter;

    public DownloadManager(BandwidthLimiter limiter, ByteCounter counter) {
        this.limiter = limiter;
        this.counter = counter;
    }

    public void validateJob(DownloadJob job) {
        if (job.getSizeInBytes() < 0) {
            throw new InvalidJobException("Job size cannot be negative");
        }
        try {
            new URL(job.getUrl());
        } catch (MalformedURLException e) {
            throw new MalformedUrlCustomException("Invalid URL: " + job.getUrl(), e);
        }
    }

    public long runAll(DownloadQueue<DownloadJob> queue) {
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(cores);

        try {
            List<List<DownloadJob>> chunks = queue.split(cores);
            List<Callable<Long>> callables = new ArrayList<>();

            for (List<DownloadJob> chunk : chunks) {
                callables.add(() -> {
                    long chunkTotal = 0;
                    for (DownloadJob job : chunk) {
                        validateJob(job);
                        try {
                            long bytes = limiter.download(job);
                            counter.addBytes(bytes);
                            chunkTotal += bytes;
                        } catch (DownloadException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return chunkTotal;
                });
            }

            List<Future<Long>> futures = new ArrayList<>();
            for (Callable<Long> callable : callables) {
                futures.add(pool.submit(callable));
            }

            for (Future<Long> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    if (e.getCause() instanceof InvalidJobException) {
                        throw (InvalidJobException) e.getCause();
                    } else if (e.getCause() instanceof MalformedUrlCustomException) {
                        throw (MalformedUrlCustomException) e.getCause();
                    }
                    throw new RuntimeException(e);
                }
            }

        } finally {
            pool.shutdown();
        }

        return counter.getTotalBytes();
    }

    public long sumSubtotals(List<? extends Number> subtotals) {
        return subtotals.stream()
                .mapToLong(Number::longValue)
                .reduce(0L, Long::sum);
    }

    public boolean allJobsWithinSizeLimit(DownloadQueue<DownloadJob> queue, long maxBytes) {
        return queue.getItems().stream()
                .allMatch(job -> job.getSizeInBytes() <= maxBytes);
    }
}
