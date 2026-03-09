package com.waarc.analytics;

import com.google.analytics.data.v1beta.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * GA4Service: Fetches active users by country for the last 30 days
 * using Google Analytics 4 Data API.
 *
 * Works on Render or locally using GA4_SERVICE_ACCOUNT environment variable.
 */
public class GA4Service {

    private static final String PROPERTY_ID = "525949883";

    public Map<String, Long> getCountryActiveUsersLast30Days() {
        Map<String, Long> countryData = new HashMap<>();

        try {
            // --------------------------------------------
            // Use environment variable instead of local file
            // --------------------------------------------
            String jsonKey = System.getenv("GA4_SERVICE_ACCOUNT");
            if (jsonKey == null || jsonKey.isEmpty()) {
                throw new RuntimeException("GA4_SERVICE_ACCOUNT environment variable is not set");
            }

            ByteArrayInputStream keyStream = new ByteArrayInputStream(jsonKey.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials credentials = ServiceAccountCredentials.fromStream(keyStream);

            // --------------------------------------------
            // Initialize GA4 client
            // --------------------------------------------
            BetaAnalyticsDataSettings settings = BetaAnalyticsDataSettings.newBuilder()
                    .setCredentialsProvider(() -> credentials)
                    .build();

            try (BetaAnalyticsDataClient client = BetaAnalyticsDataClient.create(settings)) {

                RunReportRequest request = RunReportRequest.newBuilder()
                        .setProperty("properties/" + PROPERTY_ID)
                        .addDimensions(Dimension.newBuilder().setName("country"))
                        .addMetrics(Metric.newBuilder().setName("activeUsers"))
                        .addDateRanges(DateRange.newBuilder()
                                .setStartDate("30daysAgo")
                                .setEndDate("today"))
                        .build();

                RunReportResponse response = client.runReport(request);

                // --------------------------------------------
                // Parse response
                // --------------------------------------------
                response.getRowsList().forEach(row -> {
                    String country = row.getDimensionValues(0).getValue();
                    Long activeUsers = Long.parseLong(row.getMetricValues(0).getValue());
                    countryData.put(country, activeUsers);
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch GA4 data", e);
        }

        return countryData;
    }
}