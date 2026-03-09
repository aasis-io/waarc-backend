package com.waarc.analytics;

import com.google.analytics.data.v1beta.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * GA4Service: Fetches active users by country for the last 30 days
 * using Google Analytics 4 Data API.
 *
 * Supports:
 * - Render / Docker: JSON uploaded as secret in environment variable GA4_SERVICE_ACCOUNT
 * - Local: Fallback to local file src/main/resources/ga4-service-account.json
 */
public class GA4Service {

    // Use environment variable GA4_PROPERTY_ID if set, otherwise default to 525949883
    private static final String PROPERTY_ID =
            System.getenv().getOrDefault("GA4_PROPERTY_ID", "525949883");

    /**
     * Fetch active users grouped by country for the last 30 days.
     *
     * @return Map of country -> active users count
     */
    public Map<String, Long> getCountryActiveUsersLast30Days() {
        Map<String, Long> countryData = new HashMap<>();

        try {
            // -------------------------------
            // Load credentials
            // -------------------------------
            GoogleCredentials credentials;
            String jsonKey = System.getenv("GA4_SERVICE_ACCOUNT");

            if (jsonKey != null && !jsonKey.isEmpty()) {
                // Running on Render/Docker: use env variable
                // Replace literal \n with real line breaks
                jsonKey = jsonKey.replace("\\n", "\n").trim();

                ByteArrayInputStream keyStream = new ByteArrayInputStream(
                        jsonKey.getBytes(StandardCharsets.UTF_8)
                );
                credentials = ServiceAccountCredentials.fromStream(keyStream);
            } else {
                // Running locally: load JSON from resources
                InputStream keyStream = getClass().getClassLoader()
                        .getResourceAsStream("ga4-service-account.json");

                if (keyStream == null) {
                    throw new RuntimeException("GA4 JSON file not found in resources!");
                }
                credentials = ServiceAccountCredentials.fromStream(keyStream);
            }

            // -------------------------------
            // Initialize GA4 client
            // -------------------------------
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

                // -------------------------------
                // Parse response
                // -------------------------------
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