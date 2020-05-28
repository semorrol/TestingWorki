package Utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class Report {
    private static ExtentHtmlReporter reporter;
    private static ExtentReports  extent;

    public Report()
    {
        String reportName = Utils.getCurrentDate();
        reporter = new ExtentHtmlReporter("./Reports/" + reportName + ".html");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    public static void testPassed(String testID, String testName, String testDetails)
    {
        if(!TestOPasoPrevio.getTestPassed(testID))
        {
            TestOPasoPrevio.setTestPassed(testID);

            ExtentTest logger = extent.createTest(testName);
            logger.log(Status.PASS, testDetails);
            extent.flush();
        }
    }

    public static void testPassedWithScreenshot(String testID, String testName, String testDetails, WebDriver driver) throws IOException, InterruptedException {
        if(!TestOPasoPrevio.getTestPassed(testID))
        {
            TestOPasoPrevio.setTestPassed(testID);

            ExtentTest logger = extent.createTest(testName);
            logger.log(Status.PASS, testDetails);
            String screenshotPath = Utils.takeScreenshot(driver);
            logger.pass(testDetails, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            extent.flush();
        }
    }

    public static void testFailed(String testName, String testDetails, String exception, WebDriver driver) throws IOException, InterruptedException {
        ExtentTest logger = extent.createTest(testName);

        logger.log(Status.FAIL, testDetails);
        String screenshotPath = Utils.takeScreenshot(driver);
        logger.fail(exception, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        extent.flush();
    }
}
