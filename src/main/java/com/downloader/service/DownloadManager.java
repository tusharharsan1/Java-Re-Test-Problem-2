package com.downloader.service;

import com.downloader.exception.InvalidJobException;
import com.downloader.model.DownloadJob;
import com.downloader.model.DownloadQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DownloadManager {

    private final BandwidthLimiter limiter;
    private final ByteCounter counter;

    @Autowired
    public DownloadManager(BandwidthLimiter limiter, ByteCounter counter) {
        this.limiter = limiter;
        this.counter = counter;
    }

    public void validateJob(DownloadJob job) {
        // TODO 1: public void validateJob(DownloadJob job)
        //         - If job.getSizeInBytes() < 0 -> throw InvalidJobException.
        //         - Wrap new java.net.URL(job.getUrl()) in a try-catch block.
        //         - Catch java.net.MalformedURLException and throw a new custom unchecked exception
        //           named MalformedUrlCustomException (create this exception in the exception package).
        //         (This check used to live in the DownloadJob constructor — it now lives here.)
    }

    public long runAll(DownloadQueue<DownloadJob> queue) {
        // TODO 2: public long runAll(DownloadQueue<DownloadJob> queue)
        //         1. cores = Runtime.getRuntime().availableProcessors()
        //         2. Create a fixed thread pool of size cores.
        //         3. chunks = queue.split(cores)
        //         4. For each chunk, build a Callable<Long> that:
        //              - calls validateJob(job) for each job, then limiter.download(job) (handle DownloadException)
        //              - calls counter.addBytes(...) for each
        //              - returns the chunk's byte subtotal
        //         5. SUBMIT ALL tasks first (collect Future<Long> in a list).
        //         6. Collect: try -> loop future.get(); catch interrupt/execution;
        //            finally -> ALWAYS pool.shutdown().
        //         7. Return counter.getTotalBytes().
        return 0L;
    }

    public long sumSubtotals(List<? extends Number> subtotals) {
        // TODO 3: public long sumSubtotals(List<? extends Number> subtotals)
        //         - Sum using Streams + reduce: map to long via Number::longValue, then .reduce(0L, Long::sum).
        //         - Wildcard lets it accept List<Integer>, List<Long>, etc.
        return 0L;
    }

    public boolean allJobsWithinSizeLimit(DownloadQueue<DownloadJob> queue, long maxBytes) {
        // TODO 4: public boolean allJobsWithinSizeLimit(DownloadQueue<DownloadJob> queue, long maxBytes)
        //         - Use Streams allMatch: every job's sizeInBytes <= maxBytes.
        return false;
    }
}
