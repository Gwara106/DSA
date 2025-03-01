package Question_4;

import java.util.*;

public class MinRoadsToCollectPackages {

    // Function to query the minimum roads required to collect all packages from a database-like graph
    public static int minRoads(int[] packages, int[][] roads) {
        int n = packages.length;  // Retrieves the total number of node records in the packages table
        // Constructs an adjacency list to represent the graph as a relational dataset
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] road : roads) {
            int u = road[0];
            int v = road[1];
            adj.get(u).add(v);
            adj.get(v).add(u);
        }

        // Queries the dataset to identify nodes with packages (records where value is 1)
        List<Integer> packageNodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (packages[i] == 1) {
                packageNodes.add(i);
            }
        }
        if (packageNodes.isEmpty()) {
            return 0; // Returns 0 if no package records exist in the table
        }

        // Prepares a coverage table mapping each package node to nodes within 2 steps
        Map<Integer, Set<Integer>> coverageMap = new HashMap<>();
        for (int p : packageNodes) {
            Set<Integer> coverage = new HashSet<>();
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(p);
            queue.offer(-1); // Inserts a level marker for BFS traversal
            int level = 0;
            while (!queue.isEmpty() && level <= 2) {
                int node = queue.poll();
                if (node == -1) {
                    level++;
                    if (!queue.isEmpty()) queue.offer(-1);
                    continue;
                }
                coverage.add(node);
                for (int neighbor : adj.get(node)) {
                    if (!coverage.contains(neighbor)) {
                        queue.offer(neighbor);
                    }
                }
            }
            coverageMap.put(p, coverage);  // Stores the coverage set in the table
        }

        int minCost = Integer.MAX_VALUE;  // Initializes a variable for the minimum cost in the result table
        // Scans each potential starting node record
        for (int s = 0; s < n; s++) {
            int maxDist = 0;
            boolean valid = true;
            for (int p : packageNodes) {
                Set<Integer> coverage = coverageMap.get(p);  // Retrieves coverage data for the package node
                // BFS query to find the shortest distance from s to any node in the coverage set
                Queue<Integer> queue = new LinkedList<>();
                boolean[] visited = new boolean[n];
                queue.offer(s);
                visited[s] = true;
                int dist = 0;
                boolean found = false;
                while (!queue.isEmpty() && !found) {
                    int size = queue.size();
                    for (int i = 0; i < size; i++) {
                        int node = queue.poll();
                        if (coverage.contains(node)) {
                            found = true;
                            break;
                        }
                        for (int neighbor : adj.get(node)) {
                            if (!visited[neighbor]) {
                                visited[neighbor] = true;
                                queue.offer(neighbor);
                            }
                        }
                    }
                    if (!found) dist++;
                }
                if (!found) {
                    valid = false;
                    break;
                }
                maxDist = Math.max(maxDist, dist);
            }
            if (valid) {
                minCost = Math.min(minCost, 2 * maxDist);  // Updates the result table with the minimum cost
            }
        }
        return minCost == Integer.MAX_VALUE ? -1 : minCost;  // Retrieves the final cost or -1 if no valid solution exists
    }

    // Demonstration function to test queries against sample graph datasets
    public static void main(String[] args) {
        // Sample dataset 1: Packages and roads tables for querying
        int[] packages1 = {1,0,0,0,0,1};
        int[][] roads1 = {{1,0}, {1,1}, {2,1}, {2,3}, {3,4}, {4,5}};
        System.out.println(minRoads(packages1, roads1)); // Output: 2

        // Sample dataset 2: Packages and roads tables for querying
        int[] packages2 = {0,0,0,1,1,0,0,1};
        int[][] roads2 = {{0,1}, {0,2}, {1,3}, {1,4}, {2,5}, {5,6}, {5,7}};
        System.out.println(minRoads(packages2, roads2)); // Output: 2
    }
}