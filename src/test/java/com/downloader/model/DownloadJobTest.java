package com.downloader.model;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import static org.junit.jupiter.api.Assertions.*;

class DownloadJobTest {

    @Test
    void testDownloadJobIsImmutable() {
        assertTrue(Modifier.isFinal(DownloadJob.class.getModifiers()), "DownloadJob class must be final");
        
        Field[] fields = DownloadJob.class.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                assertTrue(Modifier.isFinal(field.getModifiers()), "Field " + field.getName() + " must be final");
                assertTrue(Modifier.isPrivate(field.getModifiers()), "Field " + field.getName() + " must be private");
            }
        }
    }

    @Test
    void testJobIdGeneration() {
        DownloadJob job1 = new DownloadJob("http://example.com/1", 100);
        DownloadJob job2 = new DownloadJob("http://example.com/2", 200);

        assertTrue(job1.getJobId().startsWith("D-"));
        assertTrue(job2.getJobId().startsWith("D-"));
        assertNotEquals(job1.getJobId(), job2.getJobId());
    }

    @Test
    void testEqualsAndHashCode() throws Exception {
        DownloadJob job1 = new DownloadJob("http://test.com", 500);
        
        // Use reflection to create an identical job (same ID) to test equality logic strictly
        DownloadJob job2 = new DownloadJob("http://different.com", 999);
        Field idField = DownloadJob.class.getDeclaredField("jobId");
        idField.setAccessible(true);
        idField.set(job2, job1.getJobId());

        assertEquals(job1, job2, "Jobs with same ID must be equal");
        assertEquals(job1.hashCode(), job2.hashCode(), "HashCodes must match for equal jobs");
    }
}