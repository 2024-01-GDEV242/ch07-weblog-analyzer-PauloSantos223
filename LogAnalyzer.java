import java.util.HashMap;
import java.util.Map;

/**
 * Read web server data and analyse hourly access patterns.
 * 
 * @author Paulo Santos
 * @version    2024-03-25
 */
public class LogAnalyzer {
    // Where to calculate the hourly access counts.
    private int[] hourCounts;
    // Use a LogfileReader to access the data.
    private LogfileReader reader;
    // Where to calculate the daily access counts.
    private Map<String, Integer> dailyCounts;
    // Where to calculate the monthly access counts.
    private Map<String, Integer> monthlyCounts;
    // Where to calculate the yearly access counts.
    private Map<String, Integer> yearlyCounts;
    

    /**
     * Create an object to analyze hourly web accesses.
     * Initializes the LogAnalyzer with the specified log file.
     * 
     * @param fileName The name of the log file to be analyzed.
     */
    public LogAnalyzer(String fileName) {
        // Create the array object to hold the hourly
        // access counts.
        hourCounts = new int[24];
        // Creates the map objects to hold the daily, monthly, and yearly access counts.
        dailyCounts = new HashMap<>();
        monthlyCounts = new HashMap<>();
        yearlyCounts = new HashMap<>();
        // Create the reader to obtain the data.
        reader = new LogfileReader(fileName);
    }

    /**
     * Analyze the hourly access data from the log file.
     */
    public void analyzeHourlyData() {
        while (reader.hasNext()) {
            LogEntry entry = reader.next();
            int hour = entry.getHour();
            hourCounts[hour]++;
            // Update daily counts
            String day = entry.getDay();
            dailyCounts.put(day, dailyCounts.getOrDefault(day, 0) + 1);
            
            // Update monthly counts
            String month = entry.getMonth();
            monthlyCounts.put(month, monthlyCounts.getOrDefault(month, 0) + 1);
            
            // Update yearly counts
            String year = entry.getYear();
            yearlyCounts.put(year, yearlyCounts.getOrDefault(year, 0) + 1);
        }
    }

    /**
     * Print the hourly counts.
     * These should have been set with a prior
     * call to analyzeHourlyData.
     */
    public void printHourlyCounts() {
        System.out.println("Hr: Count");
        for (int hour = 0; hour < hourCounts.length; hour++) {
            System.out.println(hour + ": " + hourCounts[hour]);
        }
    }

    /**
     * Print the daily counts.
     */
    public void printDailyCounts() {
        System.out.println("Day: Count");
        for (String day : dailyCounts.keySet()) {
            System.out.println(day + ": " + dailyCounts.get(day));
        }
    }
    
    /**
     * Print the monthly counts.
     */
    public void printMonthlyCounts() {
        System.out.println("Month: Count");
        for (String month : monthlyCounts.keySet()) {
            System.out.println(month + ": " + monthlyCounts.get(month));
        }
    }
    
    /**
     * Print the yearly counts.
     */
    public void printYearlyCounts() {
        System.out.println("Year: Count");
        for (String year : yearlyCounts.keySet()) {
            System.out.println(year + ": " + yearlyCounts.get(year));
        }
    }
    
    /**
     * Print the lines of data read by the LogfileReader
     */
    public void printData() {
        reader.printData();
    }
    
    /**
     * Return the number of accesses recorded in the log file.
     */
    public int numberOfAccesses() {
        int total = 0;
        // Add the value in each element of hourCounts to total.
        for (int count : hourCounts) {
            total += count;
        }
        return total;
    }    
    /**
     * Return the busiest hour (hour with the highest access count).
     */
    public int busiestHour() {
        int busiestHour = 0;
        int maxCount = hourCounts[0];
        
        for (int hour = 1; hour < hourCounts.length; hour++) {
            if (hourCounts[hour] > maxCount) {
                busiestHour = hour;
                maxCount = hourCounts[hour];
            }
        }
        
        return busiestHour;
    }    
    /**
     * Return the quietest hour (hour with the lowest access count).
     */
    public int quietestHour() {
        int quietestHour = 0;
        int minCount = Integer.MAX_VALUE;
        
        for (int hour = 0; hour < hourCounts.length; hour++) {
            if (hourCounts[hour] < minCount) {
                quietestHour = hour;
                minCount = hourCounts[hour];
            }
        }
        
        return quietestHour;
    }
    
    /**
     * Return the first hour of the busiest two-hour period.
     */
    public int busiestTwoHourPeriod() {
        int busiestPeriodStartHour = 0;
        int maxCount = 0;
        
        for (int hour = 0; hour < hourCounts.length - 1; hour++) {
            int totalCount = hourCounts[hour] + hourCounts[hour + 1];
            if (totalCount > maxCount) {
                busiestPeriodStartHour = hour;
                maxCount = totalCount;
            }
        }
        
        return busiestPeriodStartHour;
    }
    
    /**
     * Return the busiest day (day with the highest access count).
     */
    public String busiestDay() {
        String busiestDay = null;
        int maxCount = 0;
        for (String day : dailyCounts.keySet()) {
            int count = dailyCounts.get(day);
            if (count > maxCount) {
                busiestDay = day;
                maxCount = count;
            }
        }
        return busiestDay;
    }
    
    /**
     * Return the quietest day (day with the lowest access count).
     */
    public String quietestDay() {
        String quietestDay = null;
        int minCount = Integer.MAX_VALUE;
        for (String day : dailyCounts.keySet()) {
            int count = dailyCounts.get(day);
            if (count < minCount) {
                quietestDay = day;
                minCount = count;
            }
        }
        return quietestDay;
    }
    /**
     * Return the busiest month (month with the highest access count).
     */
    public String busiestMonth() {
        String busiestMonth = null;
        int maxCount = 0;
        for (String month : monthlyCounts.keySet()) {
            int count = monthlyCounts.get(month);
            if (count > maxCount) {
                busiestMonth = month;
                maxCount = count;
            }
        }
        return busiestMonth;
    }
    
    /**
     * Return the quietest month (month with the lowest access count).
     */
    public String quietestMonth() {
        String quietestMonth = null;
        int minCount = Integer.MAX_VALUE;
        for (String month : monthlyCounts.keySet()) {
            int count = monthlyCounts.get(month);
            if (count < minCount) {
                quietestMonth = month;
                minCount = count;
            }
        }
        return quietestMonth;
    }
    
    /**
     * Return the average accesses per month.
     */
    public double averageAccessesPerMonth() {
        int totalAccesses = 0;
        for (int count : monthlyCounts.values()) {
            totalAccesses += count;
        }
        return (double) totalAccesses / monthlyCounts.size();
    }
}
