package Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * HtmlReportBuilder - Builds HTML email report for test execution
 */
public class HtmlReportBuilder {

    public static String build(List<ReportDataModel> reportDataList) {
        StringBuilder html = new StringBuilder();

        // Get current date & time in Cairo timezone
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy - HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Africa/Cairo"));
        String timestamp = sdf.format(new Date());

        // Calculate summary
        long totalTests = reportDataList.size();
        long passedTests = reportDataList.stream().filter(r -> "PASS".equals(r.getStatus())).count();
        long failedTests = reportDataList.stream().filter(r -> "FAIL".equals(r.getStatus())).count();
        long skippedTests = reportDataList.stream().filter(r -> "SKIPPED".equals(r.getStatus())).count();

        // HTML Header with styles
        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; background-color: #fff; }");
        html.append("h2 { color: #333; margin-bottom: 5px; }");
        html.append("p { color: #555; margin: 5px 0; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 15px; font-size: 13px; }");
        html.append("th { background-color: #f5f5f5; color: #333; font-weight: bold; padding: 10px 8px; text-align: left; border-bottom: 2px solid #ddd; }");
        html.append("td { padding: 8px; border-bottom: 1px solid #eee; vertical-align: top; }");
        html.append("tr:hover { background-color: #fafafa; }");
        html.append(".pass { color: #28a745; font-weight: bold; }");
        html.append(".fail { color: #dc3545; font-weight: bold; }");
        html.append(".skip { color: #ffc107; font-weight: bold; }");
        html.append(".comment-pass { color: #28a745; }");
        html.append(".comment-fail { color: #dc3545; }");
        html.append(".summary { background-color: #f8f9fa; padding: 10px; border-radius: 5px; margin: 15px 0; }");
        html.append(".summary span { margin-right: 20px; }");
        html.append(".number-cell { font-family: monospace; font-size: 12px; }");
        html.append("</style>");
        html.append("</head><body>");

        // Header
        html.append("<h2>Automation Test Report</h2>");
        html.append("<p><b>Hello Team,</b></p>");
        html.append("<p>Please find below the automation test execution report:</p>");
        html.append("<p><b>Report Date & Time:</b> ").append(timestamp).append("</p>");

        // Summary
        html.append("<div class='summary'>");
        html.append("<span><b>Total:</b> ").append(totalTests).append("</span>");
        html.append("<span class='pass'><b>Passed:</b> ").append(passedTests).append("</span>");
        html.append("<span class='fail'><b>Failed:</b> ").append(failedTests).append("</span>");
        html.append("<span class='skip'><b>Skipped:</b> ").append(skippedTests).append("</span>");
        html.append("</div>");

        // Table
        html.append("<table>");

        // Table Header
        html.append("<tr>");
        html.append("<th>Test Case</th>");
        html.append("<th>Status</th>");
        html.append("<th>Duration</th>");
        html.append("<th>Reserved Number</th>");
        html.append("<th>Package Type</th>");
        html.append("<th>Package Price</th>");
        html.append("<th>Subscription Time</th>");
        html.append("<th>OTP Code</th>");
        html.append("<th>Comment</th>");
        html.append("</tr>");

        // Table Rows
        for (ReportDataModel data : reportDataList) {
            html.append("<tr>");
            
            // Test Case Name
            html.append("<td>").append(safe(data.getTestCaseName())).append("</td>");
            
            // Status with color
            String status = safe(data.getStatus());
            String statusClass = "PASS".equals(status) ? "pass" : ("FAIL".equals(status) ? "fail" : "skip");
            html.append("<td class='").append(statusClass).append("'>").append(status).append("</td>");
            
            // Duration
            html.append("<td>").append(safe(data.getDuration())).append("</td>");
            
            // Reserved Number (with line break for readability)
            String reservedNum = safe(data.getReservedNumberText());
            html.append("<td class='number-cell'>").append(reservedNum.replace("\n", "<br>")).append("</td>");
            
            // Package Type
            html.append("<td>").append(safe(data.getPackageType())).append("</td>");
            
            // Package Price
            html.append("<td>").append(safe(data.getPackagePrice())).append("</td>");
            
            // Subscription Time
            html.append("<td>").append(safe(data.getSubscriptionTimestamp())).append("</td>");
            
            // OTP Code
            html.append("<td>").append(safe(data.getOtpCode())).append("</td>");
            
            // Comment with color
            String comment = data.getComment() != null ? data.getComment() : "-";
            String commentClass = comment.contains("✅") ? "comment-pass" : (comment.contains("❌") ? "comment-fail" : "");
            html.append("<td class='").append(commentClass).append("'>").append(comment).append("</td>");
            
            html.append("</tr>");
        }

        // Close Table
        html.append("</table>");

        // Footer
        html.append("<br><hr style='border: 1px solid #eee;'>");
        html.append("<p style='color: #888; font-size: 11px;'>");
        html.append("This report was automatically generated by the Shofha Test Automation System.<br>");
        html.append("© Arpu Square - Shofha Team");
        html.append("</p>");

        html.append("</body></html>");

        return html.toString();
    }

    /**
     * Helper method to handle null/empty values
     */
    private static String safe(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "N/A";
        }
        return value;
    }
}