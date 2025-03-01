// Scenario: A Multithreaded Web Crawler
// Problem:
// You need to crawl a large number of web pages to gather data or index content. Crawling each page
// sequentially can be time-consuming and inefficient.
// Goal:
// Create a web crawler application that can crawl multiple web pages concurrently using multithreading to
// improve performance.
// Tasks:
// Design the application:
// Create a data structure to store the URLs to be crawled.
// Implement a mechanism to fetch web pages asynchronously.
// Design a data storage mechanism to save the crawled data.
// Create a thread pool:
// Use the ExecutorService class to create a thread pool for managing multiple threads.
// Submit tasks:
// For each URL to be crawled, create a task (e.g., a Runnable or Callable object) that fetches the web page
// and processes the content.
// Submit these tasks to the thread pool for execution.
// Handle responses:
// Process the fetched web pages, extracting relevant data or indexing the content.
// Handle errors or exceptions that may occur during the crawling process.
// Manage the crawling queue:
// Implement a mechanism to manage the queue of URLs to be crawled, such as a priority queue or a
// breadth-first search algorithm.
// By completing these tasks, you will create a multithreaded web crawler that can efficiently crawl large
// numbers of web page

package Question_6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class WebCrawlerGUI extends JFrame {
    // GUI components for interacting with the web data retrieval system
    private JTextField urlField;                    // Input field for the initial URL record
    private JButton startButton;                    // Button to initiate the data retrieval process
    private JTextArea logArea;                      // Display area for transaction logs
    private JTextArea dataArea;                     // Display area for retrieved data records
    private JLabel statusLabel;                     // Indicator for the current system status

    // Database-like structures for managing web data retrieval
    private Queue<String> urlQueue = new LinkedBlockingQueue<>(); // Thread-safe table for pending URL records
    private Set<String> visitedUrls = Collections.synchronizedSet(new HashSet<>()); // Thread-safe table for processed URL records
    private Map<String, String> crawledData = Collections.synchronizedMap(new HashMap<>()); // Thread-safe table for storing retrieved web data
    private ExecutorService executorService;        // Worker pool for concurrent data queries
    private AtomicInteger activeTasks = new AtomicInteger(0); // Counter for ongoing database transactions
    private final int MAX_URLS = 10;                // Maximum number of URL records to process
    private final int THREAD_POOL_SIZE = 4;         // Number of concurrent workers in the pool
    private volatile boolean isCrawling = false;    // Flag to track active database operations

    // Constructor to initialize the database interface
    public WebCrawlerGUI() {
        setTitle("Web Crawler GUI");                // Sets the title of the interface window
        setSize(800, 600);                         // Configures the window dimensions
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Terminates the application on window closure
        setLayout(new BorderLayout());             // Applies a BorderLayout for organizing components

        // Control panel for managing database queries
        JPanel controlPanel = new JPanel();        // Creates a panel for query controls
        urlField = new JTextField("https://example.com", 20); // Field for entering the seed URL record
        startButton = new JButton("Start Crawling"); // Button to trigger the data retrieval process
        statusLabel = new JLabel("Status: Idle");  // Displays the current operational status
        controlPanel.add(new JLabel("Seed URL:")); // Adds a label for the URL input field
        controlPanel.add(urlField);                // Integrates the URL input field
        controlPanel.add(startButton);             // Integrates the start button
        controlPanel.add(statusLabel);             // Integrates the status indicator
        add(controlPanel, BorderLayout.NORTH);     // Positions the control panel at the top

        // Split pane for viewing logs and retrieved data
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); // Horizontal split for dual display
        logArea = new JTextArea(20, 30);           // Area for logging database transaction updates
        logArea.setEditable(false);                // Prevents editing of the log records
        dataArea = new JTextArea(20, 30);          // Area for displaying retrieved data entries
        dataArea.setEditable(false);               // Prevents editing of the data entries
        splitPane.setLeftComponent(new JScrollPane(logArea)); // Adds log area with scrolling capability
        splitPane.setRightComponent(new JScrollPane(dataArea)); // Adds data area with scrolling capability
        add(splitPane, BorderLayout.CENTER);       // Positions the split pane in the center

        // Action listener to initiate database operations
        startButton.addActionListener(e -> startCrawling()); // Triggers startCrawling on button activation

        setVisible(true);                          // Renders the interface visible
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE); // Initializes the worker pool for queries
    }

    // Method to begin querying based on the initial URL record
    private void startCrawling() {
        if (isCrawling) {                          // Checks if a query is already in progress
            JOptionPane.showMessageDialog(this, "Crawling is already in progress."); // Alerts user of ongoing operation
            return;                                // Exits if query is active
        }
        String seedUrl = urlField.getText().trim(); // Retrieves the seed URL from the input field
        if (seedUrl.isEmpty()) {                   // Validates the URL record
            JOptionPane.showMessageDialog(this, "Please enter a valid URL."); // Alerts user if record is invalid
            return;                                // Exits if invalid
        }
        urlQueue.clear();                          // Resets the pending URL table
        visitedUrls.clear();                       // Resets the processed URL table
        crawledData.clear();                       // Resets the retrieved data table
        logArea.setText("");                       // Clears the log display table
        dataArea.setText("");                      // Clears the data display table
        urlQueue.add(seedUrl);                     // Inserts the seed URL into the pending table
        isCrawling = true;                         // Marks the system as actively querying
        statusLabel.setText("Status: Crawling");   // Updates the status to reflect querying
        startButton.setEnabled(false);             // Disables the start button during query execution
        crawl();                                   // Initiates the data retrieval process
    }

    // Core logic for processing the URL table
    private void crawl() {
        while (!urlQueue.isEmpty() && visitedUrls.size() < MAX_URLS && !executorService.isShutdown()) {
            String url = urlQueue.poll();          // Retrieves the next URL record from the table
            if (url != null && !visitedUrls.contains(url)) { // Verifies the record hasn’t been processed
                visitedUrls.add(url);              // Adds the URL to the processed table
                activeTasks.incrementAndGet();     // Increments the active transaction count
                executorService.submit(new CrawlTask(url)); // Submits a query task to the worker pool
            }
        }
        // Monitors and finalizes transactions
        new Thread(() -> {
            while (activeTasks.get() > 0) {        // Waits for all transactions to complete
                try {
                    Thread.sleep(1000);            // Pauses to prevent excessive resource use
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restores interruption state
                }
            }
            shutdown();                            // Closes the worker pool when transactions complete
        }).start();                                // Initiates the transaction monitoring thread
    }

    // Method to terminate the worker pool
    private void shutdown() {
        executorService.shutdown();                // Stops accepting new database tasks
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) { // Waits 60s for tasks to finish
                executorService.shutdownNow();     // Forces termination if timeout occurs
                logArea.append("Forced shutdown due to timeout.\n"); // Logs timeout in the transaction record
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();         // Forces termination on interruption
            Thread.currentThread().interrupt();    // Restores interruption state
        }
        isCrawling = false;                        // Resets the active query flag
        statusLabel.setText("Status: Idle");       // Updates status to idle state
        startButton.setEnabled(true);              // Re-enables the start button for new queries
        logArea.append("Crawling completed. Total URLs crawled: " + visitedUrls.size() + "\n"); // Logs total processed records
    }

    // Inner class for executing URL retrieval tasks
    class CrawlTask implements Runnable {
        private String url;                        // URL record to query

        CrawlTask(String url) {
            this.url = url;                        // Assigns the URL record for processing
        }

        @Override
        public void run() {
            try {
                String content = fetchPage(url);   // Queries the web page content
                processContent(url, content);      // Processes and stores the retrieved data
                extractUrls(content);              // Extracts additional URL records from content
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> 
                    logArea.append("Error crawling " + url + ": " + e.getMessage() + "\n")); // Logs query errors in the transaction log
            } finally {
                activeTasks.decrementAndGet();     // Decrements the active transaction count
            }
        }

        // Method to retrieve web page data
        private String fetchPage(String urlString) throws Exception {
            URL url = new URL(urlString);          // Creates a URL object for the record
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(url.openStream())); // Opens a stream to fetch table data
            StringBuilder content = new StringBuilder(); // Temporary storage for retrieved data
            String line;
            while ((line = reader.readLine()) != null) { // Reads each data row
                content.append(line).append("\n"); // Appends row to the content table
            }
            reader.close();                        // Closes the data stream
            return content.toString();             // Returns the retrieved data as a single record
        }

        // Method to process and store retrieved data
        private void processContent(String url, String content) {
            String snippet = content.substring(0, Math.min(content.length(), 100)); // Extracts a data snippet (first 100 chars)
            crawledData.put(url, snippet);         // Inserts the URL and snippet into the data table
            SwingUtilities.invokeLater(() -> {     // Updates the GUI on the Event Dispatch Thread
                logArea.append("Crawled: " + url + " (Length: " + content.length() + ")\n"); // Logs the transaction
                dataArea.append("URL: " + url + "\nContent: " + snippet + "\n\n"); // Displays the retrieved record
            });
        }

        // Method to extract additional URL records
        private void extractUrls(String content) {
            String[] words = content.split("\\s+"); // Splits content into individual entries
            for (String word : words) {            // Scans each entry
                if (word.startsWith("http://") || word.startsWith("https://")) { // Identifies URL records
                    if (visitedUrls.size() < MAX_URLS && !visitedUrls.contains(word)) { // Checks table limits
                        urlQueue.add(word);        // Inserts new URL record into the pending table
                    }
                }
            }
        }
    }

    // Main method to initialize the database interface
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WebCrawlerGUI()); // Launches the interface on the EDT
    }
}