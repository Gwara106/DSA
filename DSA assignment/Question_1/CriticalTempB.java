// You have two sorted arrays of investment returns, returns1 and returns2, and a target number k. You
// want to find the kth lowest combined return that can be achieved by selecting one investment from each
// array.

// Rules:
//  The arrays are sorted in ascending order.
//  You can access any element in the arrays.

// Goal:
// Determine the kth lowest combined return that can be achieved.

// Input:
//  returns1: The first sorted array of investment returns.
//  returns2: The second sorted array of investment returns.
//  k: The target index of the lowest combined return.

// Output:
//  The kth lowest combined return that can be achieved

// Example 1:

// Input: returns1= [2,5], returns2= [3,4], k = 2
// Output: 8

// Explanation: The 2 smallest investments are are:
// - returns1 [0] * returns2 [0] = 2 * 3 = 6
// - returns1 [0] * returns2 [1] = 2 * 4 = 8
// The 2nd smallest investment is 8.

// Example 2:

// Input: returns1= [-4,-2,0,3], returns2= [2,4], k = 6

// Output: 0

// Explanation: The 6 smallest products are:
// - returns1 [0] * returns2 [1] = (-4) * 4 = -16
// - returns1 [0] * returns2 [0] = (-4) * 2 = -8
// - returns1 [1] * returns2 [1] = (-2) * 4 = -8
// - returns1 [1] * returns2 [0] = (-2) * 2 = -4
// - returns1 [2] * returns2 [0] = 0 * 2 = 0
// - returns1 [2] * returns2 [1] = 0 * 4 = 0
// The 6th smallest investment is 0.

package Question_1;

import java.util.PriorityQueue;

public class CriticalTempB {
    // Function to query the kth smallest combined return from a database-like structure
    public static int kthLowestCombinedReturn(int[] returns1, int[] returns2, int k) {
        // Initialize a min-heap as a temporary table to track combined returns with their indices
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);

        // Insert initial combined return records for each entry in returns1 into the table
        // Each record int[] contains {combined return value, returns1 index, returns2 index}
        for (int i = 0; i < returns1.length; i++) {
            minHeap.offer(new int[]{returns1[i] * returns2[0], i, 0});
        }

        // Variable to hold the kth smallest combined return fetched from the table
        int result = 0;

        // Retrieve the smallest combined return k times from the database-like table
        for (int i = 0; i < k; i++) {
            // Fetch the smallest record from the heap-based table
            int[] current = minHeap.poll();
            result = current[0]; // The combined return value from the record

            // Check if more entries exist in returns2 for the current returns1 record
            if (current[2] + 1 < returns2.length) {
                // Compute the next combined return and insert it as a new record in the table
                int nextCombinedReturn = returns1[current[1]] * returns2[current[2] + 1];
                minHeap.offer(new int[]{nextCombinedReturn, current[1], current[2] + 1});
            }
        }

        // Return the kth smallest combined return retrieved from the table
        return result;
    }

    // Demonstration function to simulate database queries for testing
    public static void main(String[] args) {
        // Query 1: Test with returns1 = {2, 5}, returns2 = {3, 4}, k = 2
        int[] returns1 = {2, 5};
        int[] returns2 = {3, 4};
        int k = 2;
        System.out.println(kthLowestCombinedReturn(returns1, returns2, k)); // Output: 8

        // Query 2: Test with returns1 = {-4, -2, 0, 3}, returns2 = {2, 4}, k = 6
        int[] returns1_2 = {-4, -2, 0, 3};
        int[] returns2_2 = {2, 4};
        int k_2 = 6;
        System.out.println(kthLowestCombinedReturn(returns1_2, returns2_2, k_2)); // Output: 0
    }
}