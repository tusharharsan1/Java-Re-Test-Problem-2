package com.downloader.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DownloadQueueTest {

    @Test
    void testSplitExactlyDivisible() {
        List<String> items = Arrays.asList("A", "B", "C", "D", "E", "F");
        DownloadQueue<String> queue = new DownloadQueue<>(items);
        
        List<List<String>> parts = queue.split(3);
        
        assertEquals(3, parts.size());
        assertEquals(2, parts.get(0).size());
        assertEquals(2, parts.get(1).size());
        assertEquals(2, parts.get(2).size());
        assertEquals("A", parts.get(0).get(0));
    }

    @Test
    void testSplitNotDivisible() {
        List<String> items = Arrays.asList("A", "B", "C", "D", "E");
        DownloadQueue<String> queue = new DownloadQueue<>(items);
        
        List<List<String>> parts = queue.split(3);
        
        assertEquals(3, parts.size());
        int totalItems = parts.stream().mapToInt(List::size).sum();
        assertEquals(5, totalItems, "No items should be lost during split");
    }

    @Test
    void testSplitMorePartsThanItems() {
        List<String> items = Arrays.asList("A", "B");
        DownloadQueue<String> queue = new DownloadQueue<>(items);
        
        List<List<String>> parts = queue.split(5);
        
        assertEquals(5, parts.size());
        assertEquals(1, parts.get(0).size());
        assertEquals(1, parts.get(1).size());
        assertEquals(0, parts.get(2).size());
        assertEquals(0, parts.get(3).size());
        assertEquals(0, parts.get(4).size());
    }

    @Test
    void testSplitEmptyQueue() {
        DownloadQueue<String> queue = new DownloadQueue<>(Collections.emptyList());
        
        List<List<String>> parts = queue.split(4);
        
        assertEquals(4, parts.size());
        for (List<String> part : parts) {
            assertTrue(part.isEmpty());
        }
    }

    // ANTI-BYPASS: Ensure they handle illegal arguments gracefully
    @Test
    void testSplitInvalidPartsThrowsException() {
        DownloadQueue<String> queue = new DownloadQueue<>(Arrays.asList("A", "B"));
        
        assertThrows(IllegalArgumentException.class, () -> queue.split(0), 
            "Splitting into 0 parts should not be allowed");
        assertThrows(IllegalArgumentException.class, () -> queue.split(-5), 
            "Splitting into negative parts should not be allowed");
    }
}