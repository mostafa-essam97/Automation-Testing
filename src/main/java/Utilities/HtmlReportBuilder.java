package Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HtmlReportBuilder {

    public static String build(List<ReportDataModel> reportDataList) {
        StringBuilder html = new StringBuilder();

        // get current date & time in Cairo timezone
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy - HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Africa/Cairo"));
        String timestamp = sdf.format(new Date());

        // HTML Header
        html.append("<html><head>");
        html.append("<style>");
        html.append("table { width:100%; border-collapse:collapse; }");
        html.append("th, td { border:1px solid #ddd; padding:8px; text-align:left; }");
        html.append("th { background-color:#f2f2f2; }");
        html.append("tr:nth-child(even) { background-color:#f9f9f9; }");
        html.append("</style>");
        html.append("</head><body>");

        html.append("<h2>Automation Test Report</h2>");
        html.append("<p><b>Hello Team,</b></p>");
        html.append("<p><b>Please find below the automation test execution report:</b></p>");
        html.append("<p><b>Report Date & Time:</b> ").append(timestamp).append("</p>");
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
            html.append("<td>").append(safe(data.getTestCaseName())).append("</td>");
            html.append("<td>").append(safe(data.getStatus())).append("</td>");
            html.append("<td>").append(safe(data.getDuration())).append("</td>");
            html.append("<td>").append(safe(data.getReservedNumberText())).append("</td>");
            html.append("<td>").append(safe(data.getPackageType())).append("</td>");
            html.append("<td>").append(safe(data.getPackagePrice())).append("</td>");
            html.append("<td>").append(safe(data.getSubscriptionTimestamp())).append("</td>");
            html.append("<td>").append(safe(data.getOtpCode())).append("</td>");
            html.append("<td>").append(data.getComment() != null ? data.getComment() : "-").append("</td>");
            html.append("</tr>");
        }

        // Close Table
        html.append("</table>");
        html.append("</body></html>");

        return html.toString();
    }

    // Helper method to handle null/empty
    private static String safe(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "N/A";
        }
        return value;
    }
}