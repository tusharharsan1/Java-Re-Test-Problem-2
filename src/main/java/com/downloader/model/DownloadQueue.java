package com.downloader.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DownloadQueue<T> {

    private final List<T> items;

    @JsonCreator
    public DownloadQueue(@JsonProperty("items") List<T> items) {
        this.items = items;
    }

    public int size() {
        return items.size();
    }

    public List<T> getItems() {
        return items;
    }

    public List<List<T>> split(int parts) {
        List<List<T>> chunks = new ArrayList<>();
        int totalSize = size();
        
        if (parts <= 0) {
            throw new IllegalArgumentException("Parts must be strictly positive");
        }
        
        if (totalSize == 0) {
            for (int i = 0; i < parts; i++) {
                chunks.add(new ArrayList<>());
            }
            return chunks;
        }

        int chunkSize = (int) Math.ceil((double) totalSize / parts);
        for (int i = 0; i < parts; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, totalSize);
            if (start >= totalSize) {
                chunks.add(new ArrayList<>());
            } else {
                chunks.add(new ArrayList<>(items.subList(start, end)));
            }
        }
        return chunks;
    }
}
