package Question_4;

//*This java algorithm is used to identify the top 3 trending hashtags from a list of of tweets in feb 2024. It first 
// stores the tweets in a list and processes them using the findTopTrendingHashtags methods. This method
// filter tweets based on their date, extracts hashtags using regular expressions (Regex) and counts their
// occurences using a HashMap. A min-heap(PriorityQueue) effieciently keeps track of only the top 3 
// hashtags by frequency. FInally, the results are sorted in descending order before being printed in a tabular format. The helper class tweet encapsulates the tweet details , and a seperate method 
// extractHashtags is used for hashtag extraction. The algorithm ensures an effiecient and structured apporach
// to finding trending hashtags while optimizing memory usage with the heap. 
//  * 
//  * 
//  * 
//  */





import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.*;

public class TrendingHashtag {

    // Demonstration function to simulate querying trending hashtags from a tweet dataset
    public static void main(String[] args) {
        // Initializes a table to store tweet records and their associated hashtags for analysis
        List<Tweet> tweets = new ArrayList<>();  // Changed to ArrayList to allow dynamic addition
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter tweets to analyze (one per line). Format: userId tweetId \"tweet text\" yyyy-MM-dd");
        System.out.println("Type 'done' when finished entering tweets.");

        while (true) {
            System.out.print("Tweet input: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("done")) {
                break;  // Exit the loop when user types 'done'
            }

            try {
                // Split input into parts: userId, tweetId, tweetText (in quotes), tweetDate
                String[] parts = input.split("\\s+", 4);  // Split on whitespace, max 4 parts
                if (parts.length != 4) {
                    System.out.println("Invalid format. Please use: userId tweetId \"tweet text\" yyyy-MM-dd");
                    continue;
                }

                int userId = Integer.parseInt(parts[0]);
                int tweetId = Integer.parseInt(parts[1]);
                String tweetText = parts[2].startsWith("\"") && parts[2].endsWith("\"") 
                    ? parts[2].substring(1, parts[2].length() - 1) : parts[2];  // Remove quotes if present
                String tweetDate = parts[3];

                // Validate date format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate.parse(tweetDate, formatter);  // Throws exception if invalid

                // Add the tweet to the table
                tweets.add(new Tweet(userId, tweetId, tweetText, tweetDate));
            } catch (Exception e) {
                System.out.println("Error parsing input: " + e.getMessage());
                System.out.println("Please use correct format: userId tweetId \"tweet text\" yyyy-MM-dd");
            }
        }

        scanner.close();

        if (tweets.isEmpty()) {
            System.out.println("No tweets entered.");
            return;
        }

        // Queries the top 3 trending hashtags from the tweet dataset
        List<Map.Entry<String, Integer>> trendingHashtags = findTopTrendingHashtags(tweets);
        // The findTopTrendingHashtags function processes the tweet table to retrieve the most frequent hashtags
        // Results are stored in a key-value table where the key is the hashtag and the value is its frequency

        // Displays the query results in a formatted table structure
        System.out.println("+------------+----------+");
        System.out.println("| Hashtag    | Count    |");
        System.out.println("+------------+----------+");

        // Iterates through the result table, retrieving hashtag keys and their counts for display
        for (Map.Entry<String, Integer> entry : trendingHashtags) {
            System.out.printf("| %-9s | %d     |\n", entry.getKey(), entry.getValue());
        }
        System.out.println("+------------+----------+");
    }

    // Function to query and rank the top trending hashtags from the tweet dataset
    public static List<Map.Entry<String, Integer>> findTopTrendingHashtags(List<Tweet> tweets) {
        Map<String, Integer> hashtagCount = new HashMap<>();  // Initializes a table to track hashtag frequencies
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");  // Sets up a date parser for record filtering

        // Step 1: Scans the tweet table to extract and count hashtags from February 2024 records
        for (Tweet tweet : tweets) {
            LocalDate tweetDate = LocalDate.parse(tweet.getTweetDate(), formatter);
            if (tweetDate.getMonthValue() == 2 && tweetDate.getYear() == 2024) {  // Filters records for February 2024
                List<String> hashtags = extractHashtags(tweet.getTweetText());

                // Updates the frequency table with hashtag occurrences
                for (String hashtag : hashtags) {
                    hashtagCount.put(hashtag, hashtagCount.getOrDefault(hashtag, 0) + 1);
                }
            }
        }

        // Step 2: Uses a min-heap table to efficiently retrieve the top 3 hashtags
        PriorityQueue<Map.Entry<String, Integer>> minHeap = new PriorityQueue<>(
            Comparator.comparingInt(Map.Entry::getValue)  // Configures the heap as a min-heap based on frequency
        );

        for (Map.Entry<String, Integer> entry : hashtagCount.entrySet()) {
            minHeap.add(entry);
            if (minHeap.size() > 3) {  // Ensures the table retains only the top 3 records
                minHeap.poll();  // Removes the record with the smallest frequency
            }
        }

        // Step 3: Extracts records from the heap and sorts them in descending order
        List<Map.Entry<String, Integer>> result = new ArrayList<>(minHeap);
        result.sort((a, b) -> b.getValue().compareTo(a.getValue()));  // Orders the result table by frequency descending

        return result;  // Returns the sorted result table
    }

    // Helper function to query hashtags from tweet text using regex filtering
    public static List<String> extractHashtags(String tweetText) {
        List<String> hashtags = new ArrayList<>();  // Initializes a temporary table for hashtag records
        Pattern pattern = Pattern.compile("#(\\w+)");  // Defines a regex filter to identify hashtag entries
        Matcher matcher = pattern.matcher(tweetText);

        while (matcher.find()) {
            hashtags.add(matcher.group());  // Adds matched hashtag records to the table
        }
        return hashtags;  // Returns the table of extracted hashtags
    }

    // Defines a helper entity representing a tweet record in the dataset
    static class Tweet {
        private int userId;
        private int tweetId;
        private String tweetText;
        private String tweetDate;

        public Tweet(int userId, int tweetId, String tweetText, String tweetDate) {
            this.userId = userId;
            this.tweetId = tweetId;
            this.tweetText = tweetText;
            this.tweetDate = tweetDate;
        }

        // Retrieves the text field from the tweet record
        public String getTweetText() {
            return tweetText;
        }

        // Retrieves the date field from the tweet record
        public String getTweetDate() {
            return tweetDate;
        }
    }
}
