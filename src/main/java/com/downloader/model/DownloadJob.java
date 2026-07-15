package com.downloader.model;

public final class DownloadJob {

    // TODO 1: Static counter (shared), starting at 0 — used to build jobId.

    // TODO 2: Final fields: jobId (String), url (String), sizeInBytes (long).

    // TODO 3: Constructor(url, sizeInBytes)
    //         - NO validation here (validation has moved to the service layer — see DownloadManager).
    //         - Increment counter, build jobId as "D-1", "D-2", ...
    //         - Assign url and sizeInBytes.

    // TODO 4: Getters for jobId, url, sizeInBytes. (No setters — immutable.)

    // TODO 5: Override equals() and hashCode() based ONLY on jobId.
}
