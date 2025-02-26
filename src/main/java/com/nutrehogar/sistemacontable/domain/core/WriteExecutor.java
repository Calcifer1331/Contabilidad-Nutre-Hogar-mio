package com.nutrehogar.sistemacontable.domain.core;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WriteExecutor {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static <T> Future<T> submitWrite(Callable<T> task) {
        return executor.submit(task);
    }

    public static void shutdown() {
        executor.shutdown();
    }
}