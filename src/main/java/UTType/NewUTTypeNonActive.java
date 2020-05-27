package UTType;

import Login.LoginWorki;
import Utils.DriversConfig;
import Utils.TestWithConfig;
import Utils.Utils;
import Utils.MyFirefoxDriver;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NewUTTypeNonActive extends TestWithConfig {

    LoginWorki loginWorkiTest = new LoginWorki(commonIni);

    Report myReport;
    ExtentHtmlReporter reporter;
    ExtentReports extent;

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    HashMap<String, String> results = new HashMap<>();

    public NewUTTypeNonActive(Wini commonIni) {
        super(commonIni);
    }

    @Override
    public HashMap<String, List<String>> getRequiredParameters() {
        HashMap<String, List<String>> requiredParameters = new HashMap<>();

        requiredParameters.put("General", new ArrayList<>(Arrays.asList("url")));
        requiredParameters.put("Login", new ArrayList<>(Arrays.asList("username", "password")));

        return requiredParameters;
    }

    @Override
    public HashMap<String, String> check() throws Exception {
        super.checkParameters();

        try {
            myFirefoxDriver = MyFirefoxDriver.getMyFirefoxDriver();
            firefoxDriver = myFirefoxDriver.getFirefoxDriver();
            firefoxWaiting = myFirefoxDriver.getFirefoxWaiting();

            loginWorkiTest.check();

            results.put(" Creates a new UT Type non active  ->  ", newUTTypenonActive());

            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return results;
        } finally {
            firefoxDriver.close();
        }

    }

    public String newUTTypenonActive() throws IOException, InterruptedException {

        myReport = Report.getMyReporter();
        reporter = myReport.getExtentHtmlReporter();
        extent = myReport.getExtentReports();

        ExtentTest logger = extent.createTest("Crea un nuevo tipo de UT marcandolo como No Activo");

        try{
            Utils.goToUTType(firefoxDriver, firefoxWaiting);

            WebElement newUTTypeButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Nuevo Tipo de UT')]"));
            newUTTypeButton.click();

            //Espera hasta que se muestre el input del nombre del nuevo tipo de ut
            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@class = 'dx-texteditor-input']")));

            WebElement UTTypeNameInput = firefoxDriver.findElement(By.xpath("//dx-validation-group//input[@class = 'dx-texteditor-input']"));
            UTTypeNameInput.sendKeys("UT type non active");

            WebElement activeCheckbox = firefoxDriver.findElement(By.xpath("//span[contains(., 'Activo')]/preceding-sibling::span"));
            activeCheckbox.click();

            WebElement createButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Crear')]"));
            createButton.click();

            //Comprueba que se ha creado y que no aparece en los activos pero si en los no activos
            try{
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'UT type non active')]")));

                logger.log(Status.FAIL, "El tipo de UT no activo aparece en la vista de tipos de UT activos");
                String screenshotPath = Utils.takeScreenshot(firefoxDriver);
                logger.fail("", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                extent.flush();

                return "ERROR. The non active UT type appears on the actives UT types view";
            } catch (Exception e) {
                Utils.showNonActiveUTTypes(firefoxDriver, firefoxWaiting);

                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'UT type non active')]")));
            }

            logger.log(Status.PASS, "Se ha creado un nuevo tipo de UT no activo");
            extent.flush();

            return "Test OK. Se ha creado un nuevo tipo de UT no activo";
        } catch(Exception e) {

            logger.log(Status.FAIL, "No se ha podido crear un nuevo tipo de UT no activo");
            String screenshotPath = Utils.takeScreenshot(firefoxDriver);
            logger.fail(ExceptionUtils.getStackTrace(e), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            extent.flush();

            e.printStackTrace();
            return e.toString() + "\nERROR. No se ha podido crear un nuevo tipo de UT no activo";
        }
    }
}