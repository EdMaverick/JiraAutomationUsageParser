package org.example;

import java.time.YearMonth;

public class MessageBuilder {

    private MessageBuilder() {
    }

    public static String build(String title, String limit, String currentUsage, String monthlyPercentage, int currentDayOfMonth) {

        // Get the current year and month to calculate the number of days in the current month
        YearMonth currentYearMonth = YearMonth.now();
        int totalDaysInMonth = currentYearMonth.lengthOfMonth(); // Total number of days in the current month

        int limitInt = Integer.parseInt(limit);
        int currentUsageInt = Integer.parseInt(currentUsage);

        // Calculate the maximum allowed usage per day
        int dailyLimit = limitInt / totalDaysInMonth;

        // Calculate the cumulative allowed usage up to the current day
        int cumulativeAllowedUsage = currentDayOfMonth * dailyLimit;

        long dailyPercentage = Math.round(((double) currentUsageInt / cumulativeAllowedUsage) * 100);

        StringBuilder result = new StringBuilder();
        result.append(title).append(":\n")
                .append("Today's limit: ").append(currentUsage).append("/").append(cumulativeAllowedUsage)
                .append(" (").append(dailyPercentage).append("%)\n")
                .append("Monthly usage: ").append(limit)
                .append(" (").append(monthlyPercentage).append("%)\n");

        // Check if current usage exceeds the cumulative allowed usage
        if (currentUsageInt > cumulativeAllowedUsage) {
            result.append("Warning! You have exceeded your usage limit for the current day!\n\n");
        } else {
            result.append("OK. You are within your usage limit.\n\n");
        }

        return result.toString();
    }
}
