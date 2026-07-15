package com.downloader.service;

import com.downloader.exception.InvalidJobException;
import com.downloader.model.DownloadJob;
import com.downloader.model.DownloadQueue;

import java.util.List;

// TODO 1: Annotate as a Spring service component.
public class DownloadManager {

    // TODO 2: Two final fields (composition):
    //         - BandwidthLimiter limiter
    //         - ByteCounter counter

    // TODO 3: Constructor injecting both fields.

    // TODO 4: public void validateJob(DownloadJob job)
    //         - If job.getSizeInBytes() < 0 -> throw InvalidJobException.
    //         - Wrap new java.net.URL(job.getUrl()) in a try-catch block.
    //         - Catch java.net.MalformedURLException and throw a new custom unchecked exception
    //           named MalformedUrlCustomException (create this exception in the exception package).
    //         (This check used to live in the DownloadJob constructor — it now lives here.)

    // TODO 5: public long runAll(DownloadQueue<DownloadJob> queue)
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

    // TODO 6: public long sumSubtotals(List<? extends Number> subtotals)
    //         - Sum using Streams + reduce: map to long via Number::longValue, then .reduce(0L, Long::sum).
    //         - Wildcard lets it accept List<Integer>, List<Long>, etc.

    // TODO 7: public boolean allJobsWithinSizeLimit(DownloadQueue<DownloadJob> queue, long maxBytes)
    //         - Use Streams allMatch: every job's sizeInBytes <= maxBytes.
}
