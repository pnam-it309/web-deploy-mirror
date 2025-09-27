package udpm.hn.server.infrastructure.processing.analytics;

import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleAnalyticsService {

    private final AnalyticsReporting analyticsReporting;
    private final String viewId;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<List<Object>> getPageViews(String startDate, String endDate) {
        try {
            // Create the DateRange object
            DateRange dateRange = new DateRange()
                    .setStartDate(startDate)
                    .setEndDate(endDate);

            // Create the Metrics object
            Metric pageviews = new Metric()
                    .setExpression("ga:pageviews")
                    .setAlias("pageviews");

            // Create the Dimensions object
            Dimension pagePath = new Dimension().setName("ga:pagePath");
            Dimension pageTitle = new Dimension().setName("ga:pageTitle");

            // Create the ReportRequest object
            ReportRequest request = new ReportRequest()
                    .setViewId(viewId)
                    .setDateRanges(List.of(dateRange))
                    .setMetrics(List.of(pageviews))
                    .setDimensions(List.of(pagePath, pageTitle));

            // Create the GetReportsRequest object
            GetReportsRequest getReport = new GetReportsRequest()
                    .setReportRequests(List.of(request));

            // Get the report
            GetReportsResponse response = analyticsReporting.reports().batchGet(getReport).execute();
            return parseResponse(response);
            
        } catch (IOException e) {
            log.error("Error fetching Google Analytics data", e);
            throw new RuntimeException("Failed to fetch analytics data", e);
        }
    }

    public List<List<Object>> getRealTimeActiveUsers() {
        try {
            // Create the Metrics object
            Metric activeUsers = new Metric()
                    .setExpression("rt:activeUsers")
                    .setAlias("activeUsers");

            // Create the ReportRequest object
            ReportRequest request = new ReportRequest()
                    .setViewId(viewId)
                    .setMetrics(List.of(activeUsers));

            // Create the GetReportsRequest object
            GetReportsRequest getReport = new GetReportsRequest()
                    .setReportRequests(List.of(request));

            // Get the report
            GetReportsResponse response = analyticsReporting.reports().batchGet(getReport).execute();
            return parseResponse(response);
            
        } catch (IOException e) {
            log.error("Error fetching real-time Google Analytics data", e);
            throw new RuntimeException("Failed to fetch real-time analytics data", e);
        }
    }

    public List<List<Object>> getTopPages(String startDate, String endDate, int limit) {
        try {
            // Create the DateRange object
            DateRange dateRange = new DateRange()
                    .setStartDate(startDate)
                    .setEndDate(endDate);

            // Create the Metrics object
            Metric pageviews = new Metric()
                    .setExpression("ga:pageviews")
                    .setAlias("pageviews");

            // Create the Dimensions object
            Dimension pagePath = new Dimension().setName("ga:pagePath");
            Dimension pageTitle = new Dimension().setName("ga:pageTitle");

            // Create the OrderBy object
            OrderBy orderBy = new OrderBy()
                    .setFieldName("ga:pageviews")
                    .setSortOrder("DESCENDING");

            // Create the ReportRequest object
            ReportRequest request = new ReportRequest()
                    .setViewId(viewId)
                    .setDateRanges(List.of(dateRange))
                    .setMetrics(List.of(pageviews))
                    .setDimensions(List.of(pagePath, pageTitle))
                    .setOrderBys(List.of(orderBy))
                    .setPageSize(limit);

            // Create the GetReportsRequest object
            GetReportsRequest getReport = new GetReportsRequest()
                    .setReportRequests(List.of(request));

            // Get the report
            GetReportsResponse response = analyticsReporting.reports().batchGet(getReport).execute();
            return parseResponse(response);
            
        } catch (IOException e) {
            log.error("Error fetching top pages from Google Analytics", e);
            throw new RuntimeException("Failed to fetch top pages data", e);
        }
    }

    private List<List<Object>> parseResponse(GetReportsResponse response) {
        List<List<Object>> result = new ArrayList<>();
        
        for (Report report : response.getReports()) {
            List<ReportRow> rows = report.getData().getRows();
            
            if (rows == null) {
                log.info("No data found for the given parameters");
                return result;
            }
            
            // Add header row
            List<Object> headerRow = new ArrayList<>();
            for (ColumnHeader header : report.getColumnHeader().getDimensions()) {
                headerRow.add(header.getName().replace("ga:", ""));
            }
            for (MetricHeaderEntry header : report.getColumnHeader().getMetricHeader().getMetricHeaderEntries()) {
                headerRow.add(header.getName());
            }
            result.add(headerRow);
            
            // Add data rows
            for (ReportRow row : rows) {
                List<Object> dataRow = new ArrayList<>();
                
                // Add dimensions
                if (row.getDimensions() != null) {
                    dataRow.addAll(row.getDimensions());
                }
                
                // Add metrics
                if (row.getMetrics() != null) {
                    for (DateRangeValues values : row.getMetrics()) {
                        dataRow.addAll(values.getValues());
                    }
                }
                
                result.add(dataRow);
            }
        }
        
        return result;
    }
}
