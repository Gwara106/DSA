// You have a network of n devices. Each device can have its own communication module installed at a
// cost of modules [i - 1]. Alternatively, devices can communicate with each other using direct connections.
// The cost of connecting two devices is given by the array connections where each connections[j] =
// [device1j, device2j, costj] represents the cost to connect devices device1j and device2j. Connections are
// bidirectional, and there could be multiple valid connections between the same two devices with different
// costs.
// Goal:
// Determine the minimum total cost to connect all devices in the network.
// Input:
// n: The number of devices.
// modules: An array of costs to install communication modules on each device.
// connections: An array of connections, where each connection is represented as a triplet [device1j,
// device2j, costj].
// Output:
// The minimum total cost to connect all devices.
// Example:
// Input: n = 3, modules = [1, 2, 2], connections = [[1, 2, 1], [2, 3, 1]] Output: 3
// Explanation:
// The best strategy is to install a communication module on the first device with cost 1 and connect the
// other devices to it with cost 2, resulting in a total cost of 3.


package Question_3;

import java.util.*;

public class MinNetworkCost {
    public static int minCostToConnect(int n, int[] modules, int[][] connections) {
        // Create adjacency list representation of device connections
        Map<Integer, List<int[]>> graph = new HashMap<>();

        // Build the graph from connections array
        for(int[] connection : connections) {
            int u = connection[0];  // Source device
            int v = connection[1];  // Target device
            int cost = connection[2]; // Connection cost
            
            graph.putIfAbsent(u, new ArrayList<>());
            graph.putIfAbsent(v, new ArrayList<>());
            
            graph.get(u).add(new int[]{v, cost});
            graph.get(v).add(new int[]{u, cost});
        }

        // Use modified Prim's algorithm considering module costs
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        boolean[] visited = new boolean[n + 1];   // Track visited devices (1-based indexing)
        int[] costToConnect = new int[n + 1];     // Minimum cost to connect each device
        Arrays.fill(costToConnect, Integer.MAX_VALUE);

        // Start with device 1, using its module cost
        minHeap.offer(new int[] {modules[0], 1});
        costToConnect[1] = modules[0];

        int totalCost = 0;  // Running total of minimum cost
        int devicesConnected = 0;  // Count connected devices
        
        while(!minHeap.isEmpty()) {
            int[] current = minHeap.poll();
            int cost = current[0];
            int node = current[1];

            if(visited[node]) continue;
            
            visited[node] = true;
            totalCost += cost;
            devicesConnected++;

            // Explore neighbors if they exist in graph
            if(graph.containsKey(node)) {
                for(int[] neighbor : graph.get(node)) {
                    int neighborNode = neighbor[0];
                    int neighborCost = neighbor[1];

                    // Choose minimum between connection cost and module cost
                    int minCost = Math.min(neighborCost, modules[neighborNode - 1]);
                    
                    if(!visited[neighborNode] && costToConnect[neighborNode] > minCost) {
                        costToConnect[neighborNode] = minCost;
                        minHeap.offer(new int[]{minCost, neighborNode});
                    }
                }
            }
        }

        // Return total cost if all devices connected, -1 otherwise
        return devicesConnected == n ? totalCost : -1;
    }

    public static void main(String[] args) {
        // Test case 1: Example from problem statement
        int n1 = 3;
        int[] modules1 = {1, 2, 2};
        int[][] connections1 = {{1, 2, 1}, {2, 3, 1}};
        System.out.println("Test 1: " + minCostToConnect(n1, modules1, connections1)); 
        // Expected output: 3

        // Test case 2: All devices need modules (no connections)
        int n2 = 3;
        int[] modules2 = {1, 1, 1};
        int[][] connections2 = {};
        System.out.println("Test 2: " + minCostToConnect(n2, modules2, connections2)); 
        // Expected output: 3

        // Test case 3: Disconnected network
        int n3 = 3;
        int[] modules3 = {1, 1, 1};
        int[][] connections3 = {{1, 2, 1}};
        System.out.println("Test 3: " + minCostToConnect(n3, modules3, connections3)); 
        // Expected output: -1

        // Test case 4: Multiple connection options
        int n4 = 4;
        int[] modules4 = {4, 4, 4, 4};
        int[][] connections4 = {{1, 2, 1}, {2, 3, 1}, {3, 4, 1}, {1, 3, 5}};
        System.out.println("Test 4: " + minCostToConnect(n4, modules4, connections4)); 
        // Expected output: 3
    }
}