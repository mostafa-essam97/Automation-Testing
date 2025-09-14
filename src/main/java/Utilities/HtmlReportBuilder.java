package Utilities;

import java.util.List;

public class HtmlReportBuilder {

    public static String build(List<ReportDataModel> reportDataList) {
        StringBuilder html = new StringBuilder();

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
        html.append("<table>");

        // Table Header
        html.append("<tr>");
        html.append("<th>Test Case</th>");
        html.append("<th>Class</th>");
        html.append("<th>Status</th>");
        html.append("<th>Duration</th>");
        html.append("<th>Username</th>");
        html.append("<th>Reserved Number</th>");
        html.append("<th>Full Number</th>");
        html.append("<th>Country Code</th>");
        html.append("<th>Package Type</th>");
        html.append("<th>Package Price</th>");
        html.append("<th>Subscription Time</th>");
        html.append("<th>OTP Code</th>");
        html.append("</tr>");

        // Table Rows
        for (ReportDataModel data : reportDataList) {
            html.append("<tr>");
            html.append("<td>").append(data.getTestCaseName()).append("</td>");
            html.append("<td>").append(data.getClassName()).append("</td>");
            html.append("<td>").append(data.getStatus()).append("</td>");
            html.append("<td>").append(data.getDuration()).append("</td>");
            html.append("<td>").append(data.getUsername()).append("</td>");
            html.append("<td>").append(data.getReservedNumberText()).append("</td>");
            html.append("<td>").append(data.getFullReservedNumber()).append("</td>");
            html.append("<td>").append(data.getCountryCode()).append("</td>");
            html.append("<td>").append(data.getPackageType()).append("</td>");
            html.append("<td>").append(data.getPackagePrice()).append("</td>");
            html.append("<td>").append(data.getSubscriptionTimestamp()).append("</td>");
            html.append("<td>").append(data.getOtpCode()).append("</td>");
            html.append("</tr>");
        }

        // Close Table
        html.append("</table>");
        html.append("</body></html>");

        return html.toString();
    }
}