package Sprint;

import Utils.MyFirefoxDriver;
import Utils.TestWithConfig;
import Utils.Utils;
import Utils.Report;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.ini4j.Wini;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class DeleteSprint extends TestWithConfig {

    Report myReport;
    ExtentHtmlReporter reporter;
    ExtentReports extent;

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    private HashMap<String, String> results = new HashMap<>();

    public DeleteSprint(Wini commonIni) {
        super(commonIni);
    }

    @Override
    public HashMap<String, List<String>> getRequiredParameters() {
        return super.getRequiredParameters();
    }

    @Override
    public HashMap<String, String> check() throws Exception {
        super.checkParameters();

        try
        {
            myFirefoxDriver = MyFirefoxDriver.getMyFirefoxDriver();
            firefoxDriver = myFirefoxDriver.getFirefoxDriver();
            firefoxWaiting = myFirefoxDriver.getFirefoxWaiting();

            results.put("Borra un sprint  ->  ", deleteSprint());

            return results;

        } catch (Exception e)
        {
            e.printStackTrace();
            return results;
        }
    }

    private String deleteSprint() throws IOException, InterruptedException {

        myReport = Report.getMyReporter();
        reporter = myReport.getExtentHtmlReporter();
        extent = myReport.getExtentReports();


        ExtentTest logger = extent.createTest("Borrar sprint");

        try
        {
            Utils.goToSprints(firefoxDriver, firefoxWaiting);

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class = 'tn-button--dropdown']")));
            WebElement thunderButton = firefoxDriver.findElement(By.xpath("//button[@class = 'tn-button--dropdown']"));
            thunderButton.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Eliminar')]")));
            WebElement deleteButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Eliminar')]"));
            deleteButton.click();

            try
            {
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Sí')]")));
                WebElement yesButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Sí')]"));
                yesButton.click();
            } catch(Exception e)
            {
                e.printStackTrace();
                return e.toString() + "\nERROR. No aparece el mensaje de confirmación de borrado de tipo";
            }

            try
            {
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'Sprint con valores obligatorios')]")));
                //si encuentra el elemento es que no se ha borrado correctamente

                logger.log(Status.FAIL, "El sprint sigue apareciendo en la tabla despues de borrarlo");
                String screenshotPath = Utils.takeScreenshot(firefoxDriver);
                logger.fail("El sprint sigue apareciendo en la tabla despues de borrarlo", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                extent.flush();


                return "ERROR. Se ha borrado el sprint pero sigue apareciendo en la tabla";
            } catch (Exception e)
            { }

            logger.log(Status.PASS, "Se ha borrado el sprint correctamente");
            extent.flush();

            return "Test OK. Se ha borrado el sprint correctamente";
        } catch (Exception e)
        {

            logger.log(Status.FAIL, "No se ha podido borrar el sprint");
            String screenshotPath = Utils.takeScreenshot(firefoxDriver);
            logger.fail(ExceptionUtils.getStackTrace(e), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            extent.flush();

            e.printStackTrace();
            return e.toString() + "\nERROR. No se ha podido borrar el sprint";
        }
    }
}
