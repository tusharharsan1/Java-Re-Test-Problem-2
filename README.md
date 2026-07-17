# RETEST 1 — PROBLEM B

# Concurrent Download Manager

## Problem Statement

You are building the core backend for a download manager (like a browser's download engine or IDM). A user queues many file-download jobs; to finish faster, the manager runs several downloads in parallel using a thread pool. Each download reports how many bytes it fetched, and the manager aggregates the total bytes downloaded once all jobs finish.

Because downloads run concurrently, correctness under parallel execution is critical: two threads updating the shared byte-total at the same time must never corrupt it, and the manager must never run more simultaneous downloads than the bandwidth limit allows. A miscount of total bytes, a lost update, or exceeding the parallel-download limit must never occur.

**Notes:**
- All `DownloadJob` objects are constructed and placed into the `DownloadQueue` **before** `runAll` is called — object creation itself is never invoked from multiple threads.
- `DownloadQueue.split(parts)`: if `parts` is greater than the number of items, some resulting sublists may be empty — this must never throw. The combined size of all sublists returned must always equal `size()`.

## Tasks

### Task 1 — Download Job Class & Generic Queue

Complete `DownloadJob` and the generic container `DownloadQueue<T>`. It must:

- Represent a `DownloadJob` with an immutable `jobId` (auto-generated via a static counter formatted as `"D-1"`, `"D-2"`, etc.), a `url`, and a `sizeInBytes`.
- Make `DownloadJob` immutable: all fields `final`, no setters, class declared `final`.
- **Note:** `DownloadJob` itself does NOT validate `sizeInBytes` in its constructor in this version — that check has moved to the service layer (see Task 4).
- Implement the generic class `DownloadQueue<T>` holding a `List<T>`, exposing `size()`, `getItems()`, and `split(int parts)` that divides the queue into roughly equal sub-lists per the rule stated above.
- Determine equality between two jobs strictly by their `jobId`.

### Task 2 — Concurrent Byte Counter (Thread-Safety)

Implement `ByteCounter`. It must:

- Maintain a shared running total of bytes downloaded and a shared count of completed jobs.
- Provide `addBytes(long bytes)` that is thread-safe (use `synchronized` or a `Lock`) — multiple threads call it concurrently, and no update may be lost.
- Expose `getTotalBytes()` and `getCompletedCount()`.

### Task 3 — Bandwidth Limiter (Semaphore)

Complete `BandwidthLimiter`. It must:

- Note: The `Semaphore` is already provided and initialized with the maximum number of parallel downloads.
- Implement `download(DownloadJob job)` which: acquires a permit from the semaphore (blocking if the limit is reached), simulates fetching (a short `Thread.sleep`), returns the number of bytes downloaded (`job.getSizeInBytes()`), and always releases the permit in a `finally` block.
- Throw a checked `DownloadException` if the download is interrupted, releasing the permit regardless.

### Task 4 — Parallel Download Engine (Executors + Callable + Future)

Implement the core logic in `DownloadManager`. It must:

- Implement `void validateJob(DownloadJob job)` — **this is the moved validation check**: 
  - If `job.getSizeInBytes() < 0`, throw `InvalidJobException`. 
  - Wrap `new java.net.URL(job.getUrl())` in a `try-catch` block. Catch `java.net.MalformedURLException` and throw a new custom exception `MalformedUrlCustomException` (which you must create in the `exception` package as an unchecked exception).
  - Call this at the start of processing each job inside `runAll`, before it's downloaded.
- **Propagation rule:** `validateJob` is unchecked and must NOT be caught inside the `Callable`. If any job in any chunk is invalid, that `Callable` throws, `Future.get()` surfaces it wrapped in an `ExecutionException`, and `runAll` must let that failure propagate out (after `shutdown()` still runs in the `finally` block) — the whole `runAll` call fails rather than silently skipping the bad job or partially returning a total. Do not swallow `InvalidJobException` or `MalformedUrlCustomException` anywhere in the chain.
- Implement `long runAll(DownloadQueue<DownloadJob> queue)` which:
  - Creates a fixed thread pool sized to `Runtime.getRuntime().availableProcessors()`.
  - Splits the queue into chunks (one per worker).
  - Wraps each chunk in a `Callable<Long>` that validates and downloads every job in the chunk (via `validateJob` then the limiter), adds the bytes to the shared `ByteCounter`, and returns the chunk's byte subtotal.
  - Submits all tasks first, then collects via `Future.get()` (submit-first / collect-last).
  - Uses `try-catch-finally` and always calls `shutdown()` in the `finally` block.
  - Returns the total bytes downloaded.
- Implement `long sumSubtotals(List<? extends Number> subtotals)` — a wildcard method summing chunk subtotals via Streams **using `reduce`** (`.reduce(0L, Long::sum)` after mapping to `long`).
- Implement `boolean allJobsWithinSizeLimit(DownloadQueue<DownloadJob> queue, long maxBytes)` — use Streams `allMatch` to check every job's `sizeInBytes` is within the limit, **inclusive** (i.e. `sizeInBytes <= maxBytes`; a job exactly equal to `maxBytes` counts as within limit).
