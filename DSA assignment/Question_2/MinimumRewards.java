// You have a team of n employees, and each employee is assigned a performance rating given in the
// integer array ratings. You want to assign rewards to these employees based on the following rules:
// Every employee must receive at least one reward.
// Employees with a higher rating must receive more rewards than their adjacent colleagues.
// Goal:
// Determine the minimum number of rewards you need to distribute to the employees.
// Input:
// ratings: The array of employee performance ratings.
// Output:
// The minimum number of rewards needed to distribute.
// Example 1:
// Input: ratings = [1, 0, 2]
// Output: 5
// Explanation: You can allocate to the first, second and third employee with 2, 1, 2 rewards respectively.
// Example 2:
// Input: ratings = [1, 2, 2]
// Output: 4
// Explanation: You can allocate to the first, second and third employee with 1, 2, 1 rewards respectively.
// The third employee gets 1 rewards because it satisfies the above two conditions.


package Question_2;

import java.util.Arrays;  // Imports the Arrays utility for database-like table operations

public class MinimumRewards {  // Defines a public class to manage reward queries from a ratings dataset
    // Function to query the total minimum rewards from a database-like ratings table
    public static int minRewards(int[] ratings) {  
        int n = ratings.length;  // Retrieves the total number of employee records in the ratings table
        if (n == 0) return 0;    // Checks if the ratings table is empty; returns 0 if no records exist
        int[] rewards = new int[n];  // Initializes a temporary table to store reward values for each employee record
        Arrays.fill(rewards, 1);     // Populates the rewards table with a default value of 1 for all entries

        // Left-to-right scan: Updates the rewards table based on higher ratings compared to left neighbors
        for (int i = 1; i < n; i++) {  // Iterates through records starting from the second entry (index 1)
            if (ratings[i] > ratings[i - 1]) {  // Queries if the current record’s rating exceeds the previous record’s
                rewards[i] = rewards[i - 1] + 1;  // Updates the current entry with one more reward than the previous entry
            }
        }

        // Right-to-left scan: Adjusts the rewards table for higher ratings compared to right neighbors
        for (int i = n - 2; i >= 0; i--) {  // Iterates backward from the second-to-last record (index n-2) to the first
            if (ratings[i] > ratings[i + 1]) {  // Queries if the current record’s rating exceeds the next record’s
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);  // Updates the current entry with the maximum of its value or one more than the next entry
            }
        }

        // Aggregates the total reward value from the rewards table
        int sum = 0;  // Initializes a variable to accumulate the sum of all reward entries
        for (int r : rewards) {  // Scans each reward entry in the rewards table
            sum += r;  // Adds the current entry’s reward to the total
        }
        return sum;  // Retrieves the total reward sum from the table
    }

    // Demonstration function to test reward queries against sample datasets
    public static void main(String[] args) {  
        // Sample dataset 1: Ratings table for querying
        int[] ratings1 = {1, 0, 2};  
        System.out.println(minRewards(ratings1));  // Queries and displays the total rewards (expected: 5)

        // Sample dataset 2: Ratings table for querying
        int[] ratings2 = {1, 2, 2};  
        System.out.println(minRewards(ratings2));  // Queries and displays the total rewards (expected: 4)
    }
}