package com.downloader.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class DownloadJob {

    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    private final String jobId;
    private final String url;
    private final long sizeInBytes;

    @JsonCreator
    public DownloadJob(@JsonProperty("url") String url, @JsonProperty("sizeInBytes") long sizeInBytes) {
        this.jobId = "D-" + COUNTER.getAndIncrement();
        this.url = url;
        this.sizeInBytes = sizeInBytes;
    }

    public String getJobId() {
        return jobId;
    }

    public String getUrl() {
        return url;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DownloadJob that = (DownloadJob) o;
        return Objects.equals(jobId, that.jobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId);
    }
}
