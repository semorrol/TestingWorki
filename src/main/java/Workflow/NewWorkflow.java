package Workflow;

import Login.LoginWorki;
import Utils.TestWithConfig;
import Utils.Utils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.ini4j.Wini;
import Utils.MyFirefoxDriver;
import Utils.Report;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class NewWorkflow extends TestWithConfig {

    LoginWorki loginWorkiTest = new LoginWorki(commonIni);

    Report myReport;
    ExtentHtmlReporter reporter;
    ExtentReports extent;

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    private HashMap<String, String> results = new HashMap<>();

    public NewWorkflow(Wini commonIni) {
        super(commonIni);
    }

    @Override
    public HashMap<String, List<String>> getRequiredParameters() {
        HashMap<String, List<String>> requiredParameters = new HashMap<>();

        return requiredParameters;
    }

    @Override
    public HashMap<String, String> check() throws Exception {
        super.checkParameters();

        try
        {
            myFirefoxDriver = MyFirefoxDriver.getMyFirefoxDriver();
            firefoxDriver = myFirefoxDriver.getFirefoxDriver();
            firefoxWaiting = myFirefoxDriver.getFirefoxWaiting();

            loginWorkiTest.check();

            results.put("Creates a workflow", createWorkflow());

            return results;
        } catch (Exception e)
        {
            e.printStackTrace();
            return results;
        }
    }

    private String createWorkflow() throws IOException, InterruptedException {

        myReport = Report.getMyReporter();
        reporter = myReport.getExtentHtmlReporter();
        extent = myReport.getExtentReports();

        ExtentTest logger = extent.createTest("Crea un nuevo workflow con los valores necesarios");

        try
        {
            Utils.goToWorkflows(firefoxDriver, firefoxWaiting);

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Nuevo WF')]")));
            WebElement nuevoWF = firefoxDriver.findElement(By.xpath("//span[contains(., 'Nuevo WF')]"));
            nuevoWF.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[span[contains(., 'Nuevo workflow')]]/following-sibling::div//input[@class = 'dx-texteditor-input']")));
            WebElement workflowNameInput = firefoxDriver.findElement(By.xpath("//div[span[contains(., 'Nuevo workflow')]]/following-sibling::div//input[@class = 'dx-texteditor-input']"));
            workflowNameInput.sendKeys("Workflow selenium");

            WebElement crearWF = firefoxDriver.findElement(By.xpath("//span[contains(., 'Crear')]"));
            crearWF.click();

            try
            {
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'Workflow selenium')]")));
            } catch (Exception e)
            {

                logger.log(Status.FAIL, "Se ha creado el workflow pero no aparece en la tabla");
                String screenshotPath = Utils.takeScreenshot(firefoxDriver);
                logger.fail(ExceptionUtils.getStackTrace(e), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                extent.flush();

                e.printStackTrace();
                return e.toString() + "\nERROR. Se ha creado el workflow pero no aparece en la tabla";
            }

            logger.log(Status.PASS, "Se ha creado un nuevo workflow con los valores necesarios");
            extent.flush();

            return "Test OK. Se ha creado un workflow";
        } catch (Exception e)
        {

            logger.log(Status.FAIL, "No se ha podido crear el workflow");
            String screenshotPath = Utils.takeScreenshot(firefoxDriver);
            logger.fail(ExceptionUtils.getStackTrace(e), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            extent.flush();

            e.printStackTrace();
            return e.toString() + "\nERROR. No se ha podido crear el workflow";
        }
    }

}
