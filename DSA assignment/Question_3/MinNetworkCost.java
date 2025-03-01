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

// Class containing logic to calculate minimum network cost using module installation and connection costs
public class MinNetworkCost {

    // Method computes minimal total cost to interconnect all devices considering both module and connection costs
    public static int minCostToConnect(int n, int[] modules, int[][] connections) {
        // Constructs adjacency list representation of device connections
        // Initialize the total cost to zero

        Map<Integer, List<int[]>> graph = new HashMap<>();

        // Processes each connection to build bidirectional links between devices
        for(int[] connection : connections) {
            int u = connection[0];  // Source device identifier
            int v = connection[1];   // Destination device identifier
            int cost = connection[2]; // Direct connection cost between devices
            
            // Ensures both devices have entries in the graph structure
            graph.putIfAbsent(u, new ArrayList<>());
            graph.putIfAbsent(v, new ArrayList<>());
            
            // Adds reciprocal connections for undirected graph representation
            graph.get(u).add(new int[]{v, cost});
            graph.get(v).add(new int[]{u, cost});
        }

        // Implements Prim's algorithm for Minimum Spanning Tree construction
        // Initialize the minimum cost to connect devices

        PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        boolean[] visited = new boolean[n+1];   // Tracks devices included in the MST
        int[] costToConnect = new int[n+1];     // Stores minimum connection costs per device
        Arrays.fill(costToConnect, Integer.MAX_VALUE);

        // Initialization with first device's module cost (device IDs start at 1)
        minHeap.offer(new int[] {modules[0], 1});
        costToConnect[1] = modules[0];

        int totalCost = 0;  // Accumulates final minimal network cost
        
        // Processes nodes until all are visited or queue is exhausted
        while(!minHeap.isEmpty()) {
            int[] current = minHeap.poll();
            int cost = current[0];
            int node = current[1];

            if(visited[node]) continue;  // Skips already processed devices
            
            visited[node] = true;       // Marks device as included in MST
            totalCost += cost;         // Accumulates the connection/module cost

            // Updates costs for neighboring devices if better connection found
            if(graph.containsKey(node)) {
                for(int[] neighbor : graph.get(node)) {
                    int neighborNode = neighbor[0];
                    int neighborCost = neighbor[1];

                    // Updates cost if neighbor unvisited and new cost is lower
                    if(!visited[neighborNode] && costToConnect[neighborNode] > neighborCost) {
                        costToConnect[neighborNode] = neighborCost;
                        minHeap.offer(new int[]{neighborCost, neighborNode});
                    }
                }
            }
        }

        // Validates full network connectivity
        // Return -1 if the network is disconnected

        for(int i = 1; i <= n; i++) {
            if(!visited[i]) return -1;  // Indicates disconnected network
        }
        return totalCost;
    }

    // (Test cases remain unchanged...)
}
