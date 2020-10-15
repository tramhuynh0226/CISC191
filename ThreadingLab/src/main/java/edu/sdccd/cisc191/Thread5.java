package edu.sdccd.cisc191;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import java.util.ArrayList;

import java.util.concurrent.*;

public class Thread5 {


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i <= 10000; i++) {
            executorService.submit(new Runnable() {

                public void run() {
                    System.out.println("ExecutorService");
                }
            });
        }

        executorService.shutdown();
    }

}
