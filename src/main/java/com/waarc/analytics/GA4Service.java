package com.waarc.analytics;

import com.google.analytics.data.v1beta.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class GA4Service {

    private static final String PROPERTY_ID = "525949883";

    public Map<String, Long> getCountryActiveUsersLast30Days() {
        Map<String, Long> countryData = new HashMap<>();

        try {
            InputStream keyFile = getClass().getResourceAsStream("/ga4-service-account.json");
            GoogleCredentials credentials = ServiceAccountCredentials.fromStream(keyFile);

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

                response.getRowsList().forEach(row -> {
                    String country = row.getDimensionValues(0).getValue();
                    Long activeUsers = Long.parseLong(row.getMetricValues(0).getValue());
                    String value = row.getMetricValues(0).getValue();
                    countryData.put(country, activeUsers);
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return countryData;
    }
}