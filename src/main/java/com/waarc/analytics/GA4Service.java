package com.waarc.analytics;

import com.google.analytics.data.v1beta.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class GA4Service {

    private static final String PROPERTY_ID = "525949883";

    public Map<String, Long> getCountryActiveUsersLast30Days() {
        Map<String, Long> countryData = new HashMap<>();

        try {
            // Must be set in environment


            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            String base64Key = dotenv.get("GA4_KEY");            if (base64Key == null || base64Key.isEmpty()) {
                throw new RuntimeException("GA4_KEY environment variable is not set!");
            }

            byte[] decodedKey = Base64.getDecoder().decode(base64Key);
            InputStream keyStream = new ByteArrayInputStream(decodedKey);

            GoogleCredentials credentials = ServiceAccountCredentials.fromStream(keyStream);

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
                    countryData.put(country, activeUsers);
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return countryData;
    }
}