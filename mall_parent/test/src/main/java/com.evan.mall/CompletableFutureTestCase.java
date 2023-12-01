package com.evan.mall;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CompletableFutureTestCase {

    private final Executor executor = new ThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));

    @SneakyThrows
    @Test
    public void test1() {
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getId());
        });

        CompletableFuture<Integer> exceptionally = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            return 1024;
        }).whenComplete((t, u) -> {
            System.out.println("t=>" + t);
            System.out.println("u=>" + u);
            throw new RuntimeException("异常");
        }).exceptionally((e) -> {
            System.out.println(e.getMessage());
            return 233;
        });
        exceptionally.complete(233);
        Thread.sleep(1000);
    }


    @Test
    public void test2() {

        CompletableFuture<String> completableFutureA = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return "404";
            }
        }, executor);
        completableFutureA.thenAcceptAsync(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println("s1=>" + s);
            }
        }, executor);

        completableFutureA.thenAcceptAsync(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println("s2=>" + s);
            }
        }, executor);
    }
}
