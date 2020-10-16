import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.ArrayList;

public class countOddNumber {

    /** Check if the number can dividable by 2 */
    private static boolean isOdd(int x) {
        if (x % 2 == 0)
            return false;
        return true;
    }

    /** Loop each number then check if it is odd. Then return how many odd number from min to max */
    private static int countOdd (int min,int max) {
        int count = 0;
        for(int i = min; i <= max; ++i)
            if(isOdd(i))
                count++;
        return count;
    }

    /** This class object will count odd number from min and max that inherit from parameters */
    private static class CountTask implements Callable<Integer> {
        int min, max;
        public CountTask(int min, int max) {
            this.min = min;
            this.max = max;
        }
        public Integer call() {
            int count = countOdd(min,max);
            return count;
        }
    }

    /**
     This method will divide the count tasks based on number of available processors, then will give accordingly min-max
     number for each thread to count.
     For example: If there is one thread, then it will count from 1 to 100. But if there are
     2 threads, then 1st thread will count from 1 to 50, then 2nd threads will count from 51 to 100.
     */
    private static void countOddWithExecutor(int min, int max) {

        long startTime = System.currentTimeMillis();
        // Total number to count from min-max
        int numberofCounts = max - min;

        // Get number of available processors
        int processors = Runtime.getRuntime().availableProcessors();
        // Create number of threads based on processors number
        ExecutorService executor = Executors.newFixedThreadPool(processors);
        // Calculate number of portion size each thread has to do
        double numbCount = (double)numberofCounts/processors;
        // Round up then convert the portion number size
        int increment = (int)Math.ceil(numbCount);
        // Create Future to save all threads results
        ArrayList<Future<Integer>> totalOddCount = new ArrayList<>();

        // Start of each range size of one subtask
        int start = min;
        // End of each range size of one subtask
        int end;

        for (int i = 0; i < processors; i++) {

            // If it is last thread, it will get the max number
            if (i == processors-1) {
                end = max;

                // End of range size gets number from each portion of increment
            } else {
                end = ((i+1)*increment);
            }

            // Create oneCount task from the CountTask class
            CountTask oneThreadCount = new CountTask(start, end);
            // Each oneThreadCount will be add into OddCount of each thread
            Future<Integer> oddCount = executor.submit( oneThreadCount );
            // Each thread count will be added into totalOddCount
            totalOddCount.add(oddCount);
            /* After each loop, the start of each range size will be the end of previous
            loop +1 for the next loop */
            start = end + 1;
        }

        // executor shut down after all threads are done with their tasks
        executor.shutdown();

        // Add all the results from all the threads
        int totalOddNumber = 0;
        for ( Future<Integer> res : totalOddCount) {
            try {
                totalOddNumber += res.get();
                // Catch error
            } catch (Exception e) {
                System.out.println("Error occurred while computing: " + e);
            }
        }

        // Calculate the time it takes for all the threads to run until this line
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("\nThere are " + totalOddNumber + " odd number from " + min + " to " + max);
        System.out.println("\nTotal elapsed time:  " + (elapsedTime/1000.0) + " seconds.");
    }

    /**
     Get min and max value to count odd number from user
     */
    public static void main(String[] args) {
        int processors = Runtime.getRuntime().availableProcessors();
        System.out.println("Total processors in your computer " + processors);
        Scanner scnr = new Scanner(System.in);
        System.out.println("User put min value: ");
        int min = scnr.nextInt();
        System.out.println("User put max value: ");
        int max = scnr.nextInt();

        countOddWithExecutor(min, max);
    }

}