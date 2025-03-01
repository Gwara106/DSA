// You are given a class NumberPrinter with three methods: printZero, printEven, and printOdd.
// These methods are designed to print the numbers 0, even numbers, and odd numbers, respectively.
// Task:
// Create a ThreadController class that coordinates three threads:
// 5. ZeroThread: Calls printZero to print 0s.
// 6. EvenThread: Calls printEven to print even numbers.
// 7. OddThread: Calls printOdd to print odd numbers.
// These threads should work together to print the sequence "0102030405..." up to a specified number n.
// The output should be interleaved, ensuring that the numbers are printed in the correct order.
// Example:
// If n = 5, the output should be "0102030405".
// Constraints:
//  The threads should be synchronized to prevent race conditions and ensure correct output.
//  The NumberPrinter class is already provided and cannot be modified.


package Question_6;

import java.util.concurrent.Semaphore;

public class ThreadController {
    // Semaphores to manage access to the database-like print sequence
    private Semaphore zeroSem = new Semaphore(1); // Permit for ZeroThread to initiate record printing (1 permit initially)
    private Semaphore evenSem = new Semaphore(0); // No initial permit for EvenThread to access the table
    private Semaphore oddSem = new Semaphore(0);  // No initial permit for OddThread to access the table

    private int n;                // Maximum value in the sequence table
    private NumberPrinter printer; // Instance of NumberPrinter to handle record output

    // Constructor to initialize the sequence table and printer reference
    public ThreadController(int n, NumberPrinter printer) {
        this.n = n;               // Sets the upper limit for the sequence records
        this.printer = printer;   // Assigns the NumberPrinter instance for output operations
    }

    // Method for ZeroThread to insert 0s into the sequence table
    public void printZero() throws InterruptedException {
        for (int i = 0; i <= n; i++) {           // Iterates n+1 times to insert 0 before each record
            zeroSem.acquire();                   // Acquires a permit to access the table
            printer.printZero();                 // Inserts a 0 record into the output stream
            if (i < n) {                         // Releases permits only if more records remain
                if (i % 2 == 0) {                // Even index: next record is odd
                    oddSem.release();            // Grants OddThread access to the table
                } else {                         // Odd index: next record is even
                    evenSem.release();           // Grants EvenThread access to the table
                }
            }
        }
    }

    // Method for EvenThread to append even-numbered records
    public void printEven() throws InterruptedException {
        for (int i = 2; i <= n; i += 2) {        // Scans even records from 2 to n
            evenSem.acquire();                   // Waits for permission to update the table
            printer.printEven(i);                // Adds the even record to the output
            zeroSem.release();                   // Allows ZeroThread to append the next 0 record
        }
    }

    // Method for OddThread to append odd-numbered records
    public void printOdd() throws InterruptedException {
        for (int i = 1; i <= n; i += 2) {        // Scans odd records from 1 to n
            oddSem.acquire();                    // Waits for permission to update the table
            printer.printOdd(i);                 // Adds the odd record to the output
            zeroSem.release();                   // Allows ZeroThread to append the next 0 record
        }
    }

    // Main method to simulate a database transaction test
    public static void main(String[] args) {
        int n = 5;                            // Defines the maximum sequence value for the test table
        NumberPrinter printer = new NumberPrinter(); // Initializes the output handler for records
        ThreadController controller = new ThreadController(n, printer); // Sets up the transaction controller

        // Creates worker threads to process the sequence table
        Thread zeroThread = new Thread(() -> {   // ZeroThread worker
            try {
                controller.printZero();          // Executes the zero-record insertion task
            } catch (InterruptedException e) {
                e.printStackTrace();             // Logs any transaction interruptions
            }
        });

        Thread evenThread = new Thread(() -> {   // EvenThread worker
            try {
                controller.printEven();          // Executes the even-record insertion task
            } catch (InterruptedException e) {
                e.printStackTrace();             // Logs any transaction interruptions
            }
        });

        Thread oddThread = new Thread(() -> {    // OddThread worker
            try {
                controller.printOdd();           // Executes the odd-record insertion task
            } catch (InterruptedException e) {
                e.printStackTrace();             // Logs any transaction interruptions
            }
        });

        // Initiates all worker threads to process the table
        zeroThread.start();                   // Begins ZeroThread transaction
        evenThread.start();                   // Begins EvenThread transaction
        oddThread.start();                    // Begins OddThread transaction

        // Ensures all table updates are completed
        try {
            zeroThread.join();                // Waits for ZeroThread to complete its updates
            evenThread.join();                // Waits for EvenThread to complete its updates
            oddThread.join();                 // Waits for OddThread to complete its updates
        } catch (InterruptedException e) {
            e.printStackTrace();              // Logs any interruptions during completion
        }
    }
}

// Mock NumberPrinter class to simulate database output operations
class NumberPrinter {
    public void printZero() {
        System.out.print("0");            // Outputs a zero record to the console stream
    }

    public void printEven(int num) {
        System.out.print(num);            // Outputs an even record to the console stream
    }

    public void printOdd(int num) {
        System.out.print(num);            // Outputs an odd record to the console stream
    }
}