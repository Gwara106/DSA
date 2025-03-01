// You have a material with n temperature levels. You know that there exists a critical temperature f where
// 0 <= f <= n such that the material will react or change its properties at temperatures higher than f but
// remain unchanged at or below f.

// Rules:
//  You can measure the material's properties at any temperature level once.
//  If the material reacts or changes its properties, you can no longer use it for further measurements.
//  If the material remains unchanged, you can reuse it for further measurements.

// Goal:
// Determine the minimum number of measurements required to find the critical temperature.

// Input:
//  k: The number of identical samples of the material.
//  n: The number of temperature levels.

// Output:
//  The minimum number of measurements required to find the critical temperature.

// Example 1:
// Input: k = 1, n = 2
// Output: 2
// Explanation:
// Check the material at temperature 1. If its property changes, we know that f = 0.
// Otherwise, raise temperature to 2 and check if property changes. If its property changes, we know that f =
// 1.If its property changes at temperature, then we know f = 2.
// Hence, we need at minimum 2 moves to determine with certainty what the value of f is.
// Example 2:
// Input: k = 2, n = 6
// Output: 3
// Example 3:
// Input: k = 3, n= 14
// Output: 4

package Question_1;

public class CriticalTempA {
    // Core function to query the minimum tests needed from a database-like structure
    public static int minMeasurements(int k, int n) {
        // Set up a 2D array as a temporary table to store test counts for each subquery
        int[][] dp = new int[n + 1][k + 1];

        // Establish base entries in the table
        // Zero temperature entries require no tests in the database
        for (int i = 0; i <= k; i++) {
            dp[0][i] = 0;
        }
        // A single temperature entry needs just one test recorded
        for (int i = 0; i <= k; i++) {
            dp[1][i] = 1;
        }
        // With one sample, test counts match the number of temperature entries
        for (int i = 0; i <= n; i++) {
            dp[i][1] = i;
        }

        // Populate the table by simulating database updates with dynamic logic
        for (int i = 2; i <= n; i++) { // Process each temperature record
            for (int j = 2; j <= k; j++) { // Process each sample record
                dp[i][j] = Integer.MAX_VALUE; // Set an initial high value for the entry
                for (int x = 1; x <= i; x++) { // Query each possible temperature test point
                    // Compute the worst-case test count for this entry
                    // Reaction at temperature x leaves (x-1) entries with (j-1) samples
                    // No reaction leaves (i-x) entries with j samples
                    int res = 1 + Math.max(dp[x - 1][j - 1], dp[i - x][j]);
                    // Update the table entry with the smallest worst-case value
                    dp[i][j] = Math.min(dp[i][j], res);
                }
            }
        }

        // Fetch the final test count from the table at dp[n][k]
        return dp[n][k];
    }

    // Demonstration function to simulate database queries for testing
    public static void main(String[] args) {
        // Query 1: Test with 1 sample and 2 temperatures
        System.out.println(minMeasurements(1, 2)); // Output: 2
        // Query 2: Test with 2 samples and 6 temperatures
        System.out.println(minMeasurements(2, 6)); // Output: 3
        // Query 3: Test with 3 samples and 14 temperatures
        System.out.println(minMeasurements(3, 14)); // Output: 4
    }
}