package com.rohangulati.agoda;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class ConcurrentRunner {

  private ConcurrentRunner() {
    // no instance creation
  }

  public static <T> Stream<T> run(Callable<T> task, int numTask) {
    CountDownLatch latch = new CountDownLatch(numTask);
    ExecutorService executorService = Executors.newFixedThreadPool(numTask);
    List<Future<T>> result = new ArrayList<>();
    for (int i = 0; i < numTask; i++) {
      Future<T> future = executorService.submit(new ConcurrentRunnerTask<>(latch, task));
      result.add(future);
    }
    return result.stream()
        .map(
            f -> {
              try {
                return f.get();
              } catch (InterruptedException | ExecutionException ignore) {
              }
              return null;
            });
  }

  @Data
  @AllArgsConstructor
  private static class ConcurrentRunnerTask<T> implements Callable<T> {
    private final CountDownLatch latch;
    private final Callable<T> task;

    @Override
    public T call() throws Exception {
      latch.countDown();
      // wait until all thread are at this barrier
      latch.await();
      // execute the task concurrently
      return task.call();
    }
  }
}
