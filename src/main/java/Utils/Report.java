package Utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class Report {
    private static ExtentHtmlReporter reporter;
    private static ExtentReports  extent;
    private static Report myReport;


    public static Report getMyReporter()
    {
        if(myReport == null)
        {
            myReport = new Report();
        }
        return myReport;
    }

    public Report()
    {
        String reportName = Utils.getCurrentDate();
        reporter = new ExtentHtmlReporter("./Reports/" + reportName + ".html");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    public ExtentHtmlReporter getExtentHtmlReporter()
    {
        return reporter;
    }

    public ExtentReports getExtentReports()
    {
        return extent;
    }
}
