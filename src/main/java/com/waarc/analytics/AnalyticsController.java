package com.waarc.analytics;

import com.waarc.analytics.GA4Service;
import io.javalin.Javalin;

public class AnalyticsController {

    private final GA4Service ga4Service;

    public AnalyticsController(Javalin app) {
        this.ga4Service = new GA4Service();

        // Register API endpoint
        app.get("/analytics/country", ctx -> {
            ctx.json(ga4Service.getCountryActiveUsersLast30Days());
        });
    }
}