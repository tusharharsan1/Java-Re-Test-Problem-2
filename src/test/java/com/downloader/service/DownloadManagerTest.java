package com.downloader.service;

import com.downloader.exception.InvalidJobException;
import com.downloader.exception.MalformedUrlCustomException;
import com.downloader.model.DownloadJob;
import com.downloader.model.DownloadQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DownloadManagerTest {

    @Mock
    private BandwidthLimiter limiter;

    @Mock
    private ByteCounter counter;

    @InjectMocks
    private DownloadManager manager;

    @BeforeEach
    void setUp() {
        // Reset mocks handled by MockitoExtension
    }

    @Test
    void testValidateJobValid() {
        DownloadJob job = new DownloadJob("http://example.com/file", 100);
        assertDoesNotThrow(() -> manager.validateJob(job));
    }

    @Test
    void testValidateJobNegativeSize() {
        DownloadJob job = new DownloadJob("http://example.com/file", -10);
        assertThrows(InvalidJobException.class, () -> manager.validateJob(job));
    }

    @Test
    void testValidateJobMalformedUrl() {
        DownloadJob job = new DownloadJob("not-a-valid-url", 100);
        assertThrows(MalformedUrlCustomException.class, () -> manager.validateJob(job));
    }

    @Test
    void testRunAllSuccess() throws Exception {
        DownloadJob job1 = new DownloadJob("http://example.com/1", 100);
        DownloadJob job2 = new DownloadJob("http://example.com/2", 200);
        DownloadQueue<DownloadJob> queue = new DownloadQueue<>(Arrays.asList(job1, job2));

        when(limiter.download(any(DownloadJob.class))).thenAnswer(invocation -> {
            DownloadJob j = invocation.getArgument(0);
            return j.getSizeInBytes();
        });
        when(counter.getTotalBytes()).thenReturn(300L);

        long total = manager.runAll(queue);

        assertEquals(300L, total);
        verify(limiter, times(2)).download(any(DownloadJob.class));
        verify(counter).addBytes(100);
        verify(counter).addBytes(200);
    }

    @Test
    void testRunAllWithInvalidJobPropagatesException() {
        DownloadJob validJob = new DownloadJob("http://example.com/1", 100);
        DownloadJob invalidJob = new DownloadJob("invalid-url", 200);
        DownloadQueue<DownloadJob> queue = new DownloadQueue<>(Arrays.asList(validJob, invalidJob));

        assertThrows(MalformedUrlCustomException.class, () -> manager.runAll(queue));
    }

    @Test
    void testSumSubtotals() {
        List<Integer> subs = Arrays.asList(10, 20, 30);
        long sum = manager.sumSubtotals(subs);
        assertEquals(60L, sum);
    }

    @Test
    void testAllJobsWithinSizeLimit() {
        DownloadJob job1 = new DownloadJob("http://a.com", 100);
        DownloadJob job2 = new DownloadJob("http://b.com", 200);
        DownloadQueue<DownloadJob> queue = new DownloadQueue<>(Arrays.asList(job1, job2));

        assertTrue(manager.allJobsWithinSizeLimit(queue, 200));
        assertFalse(manager.allJobsWithinSizeLimit(queue, 150));
    }
}
