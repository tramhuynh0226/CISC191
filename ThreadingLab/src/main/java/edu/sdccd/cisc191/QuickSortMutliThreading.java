package edu.sdccd.cisc191;

// Java program for the above approach
import java.io.*;
import java.util.Random;
import java.util.concurrent.*;

public class QuickSortMutliThreading
        extends RecursiveTask<Integer> {

    int start, end;
    int[] arr;

    /**
     * Finding random pivoted and partition
     * array on a pivot.
     * There are many different
     * partitioning algorithms.
     * @param start
     * @param end
     * @param arr
     * @return
     */
    private int partion(int start, int end, int[] arr) {

        int i = start, j = end;

        // Decide random pivot
        int pivote = new Random()
                .nextInt(j - i)
                + i;

        // Swap the pivote with end
        // element of array;
        int t = arr[j];
        arr[j] = arr[pivote];
        arr[pivote] = t;
        j--;

        // Start partioning
        while (i <= j) {

            if (arr[i] <= arr[end]) {
                i++;
                continue;
            }

            if (arr[j] >= arr[end]) {
                j--;
                continue;
            }

            t = arr[j];
            arr[j] = arr[i];
            arr[i] = t;
            j--;
            i++;
        }

        // Swap pivote to its
        // correct position
        t = arr[j + 1];
        arr[j + 1] = arr[end];
        arr[end] = t;
        return j + 1;
    }

    // Function to implement
    // QuickSort method
    public QuickSortMutliThreading(int start, int end, int[] arr)
    {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute()
    {
        // Base case
        if (start >= end)
            return null;

        // Find partion
        int p = partion(start, end, arr);


        // Divide array
        QuickSortMutliThreading left
                = new QuickSortMutliThreading(start,p - 1,arr);

        QuickSortMutliThreading right
                = new QuickSortMutliThreading(p + 1,end,arr);

        // Left subproblem as separate thread
        left.fork();
        right.compute();

        // Wait untill left thread complete
        left.join();

        // We don't want anything as return
        return null;
    }

    // Driver Code
    public static void main(String args[])
    {
        int[] arr = { 54, 64, 95, 82, 12, 32, 63, 100, 55, 147, 854, 1000, 1, 60 };
        int n = arr.length;

        // Forkjoin ThreadPool to keep
        // thread creation as per resources
        ForkJoinPool pool = ForkJoinPool.commonPool();

        // Start the first thread in fork
        // join pool for range 0, n-1
        pool.invoke(new QuickSortMutliThreading(0, n - 1, arr));

        ExecutorService executor = Executors.newFixedThreadPool(10);
        QuickSortMutliThreading quickSort = new QuickSortMutliThreading(0, n - 1, arr);
        executor.execute( quickSort );

        // Print shorted elements
        for (int i = 0; i < n; i++)
            System.out.print(arr[i] + " ");
    }
}
