package UTType;

import Login.LoginWorki;
import Utils.MyFirefoxDriver;
import Utils.TestWithConfig;
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
import Utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class DeleteUTTypeCheckAlert extends TestWithConfig {

    NewUTTypeByDefault newUTTypeByDefaultTest = new NewUTTypeByDefault(commonIni);

    Report myReport;
    ExtentHtmlReporter reporter;
    ExtentReports extent;

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    HashMap<String, String> results = new HashMap<>();

    public DeleteUTTypeCheckAlert(Wini commonIni) {
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

        try{
            myFirefoxDriver = MyFirefoxDriver.getMyFirefoxDriver();
            firefoxDriver = myFirefoxDriver.getFirefoxDriver();
            firefoxWaiting = myFirefoxDriver.getFirefoxWaiting();

            newUTTypeByDefaultTest.check();

            results.put("Eliminacion de un tipo sin UT asociadas. Aparece antes un mensaje de confirmacion", deleteUTTypeCheckAlert());

            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return results;
        }

    }

    public String deleteUTTypeCheckAlert() throws IOException, InterruptedException {

        myReport = Report.getMyReporter();
        reporter = myReport.getExtentHtmlReporter();
        extent = myReport.getExtentReports();

        ExtentTest logger = extent.createTest("Borra un tipo de Ut y comprueba que aparece la alerta de seguridad");

        try {
            Utils.goToUTType(firefoxDriver, firefoxWaiting);
            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'UT type by default')]/preceding-sibling::td//button[@class = 'tn-button--dropdown']")));
            WebElement thunderButton = firefoxDriver.findElement(By.xpath("//td[contains(., 'UT type by default')]/preceding-sibling::td//button[@class = 'tn-button--dropdown']"));
            thunderButton.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'UT type by default')]/preceding-sibling::td//span[contains(., 'Eliminar')]")));
            WebElement deleteButton = firefoxDriver.findElement(By.xpath("//td[contains(., 'UT type by default')]/preceding-sibling::td//span[contains(., 'Eliminar')]"));
            deleteButton.click();

            try
            {
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Sí')]")));
                WebElement yesButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Sí')]"));
                yesButton.click();
            } catch(Exception e)
            {

                logger.log(Status.FAIL, "No aparece el mensaje de alerta de borrado de tipo");
                String screenshotPath = Utils.takeScreenshot(firefoxDriver);
                logger.fail(ExceptionUtils.getStackTrace(e), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                extent.flush();

                e.printStackTrace();
                return e.toString() + "\nERROR. No aparece el mensaje de confirmación de borrado de tipo";
            }

            logger.log(Status.PASS, "El borrado de tipos funciona correctamente");
            extent.flush();

            return "Test OK. El borrado de tipos funciona correctamente";
        } catch (Exception e) {

            logger.log(Status.FAIL, "Ha habido un problema al intentar borrar el tipo de UT");
            String screenshotPath = Utils.takeScreenshot(firefoxDriver);
            logger.fail(ExceptionUtils.getStackTrace(e), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            extent.flush();

            e.printStackTrace();
            return e.toString() + "\nERROR. Excepcion inesperada";
        }
    }
}
