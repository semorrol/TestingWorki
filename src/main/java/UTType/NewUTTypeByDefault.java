package UTType;

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
import Utils.MyFirefoxDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NewUTTypeByDefault extends TestWithConfig {
    static String url;
    static String headless;
    static String username;
    static String password;

    Report myReport;
    ExtentHtmlReporter reporter;
    ExtentReports extent;

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    HashMap<String, String> results = new HashMap<>();

    public NewUTTypeByDefault(Wini commonIni) {
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

        url = commonIni.get("General", "url");
        headless = commonIni.get("General", "headless");
        username = commonIni.get("Login", "username");
        password = commonIni.get("Login", "password");

        try {
            myFirefoxDriver = MyFirefoxDriver.getMyFirefoxDriver();
            firefoxDriver = myFirefoxDriver.getFirefoxDriver();
            firefoxWaiting = myFirefoxDriver.getFirefoxWaiting();

            results.put(" Creates a new UT Type with the configuration by default  ->  ", newUTTYpeByDefault());

            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return results;
        }
    }

    public String newUTTYpeByDefault() throws IOException, InterruptedException {
        myReport = Report.getMyReporter();
        reporter = myReport.getExtentHtmlReporter();
        extent = myReport.getExtentReports();


        ExtentTest logger = extent.createTest("Crea un nuevo tipo de UT con los valores por defecto");

        try{
            //Este metodo hace los pasos necesarios para crear un nuevo tipo de UT, a partir de ahi se configuran los parametros
            Utils.goToUTType(firefoxDriver, firefoxWaiting);

            WebElement newUTTypeButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Nuevo Tipo de UT')]"));
            newUTTypeButton.click();

            //Espera hasta que se muestre el input del nombre del nuevo tipo de ut
            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@class = 'dx-texteditor-input']")));

            WebElement UTTypeNameInput = firefoxDriver.findElement(By.xpath("//dx-validation-group//input[@class = 'dx-texteditor-input']"));
            UTTypeNameInput.sendKeys("UT type by default");

            WebElement createButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Crear')]"));
            createButton.click();

            //Comprueba que se ha creado el tipo de ut
            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'UT type by default')]")));

            logger.log(Status.PASS, "Se ha creado el tipo de UT con los valores por defecto");
            extent.flush();

            return "Test OK. A new UT type by default was created";
        } catch(Exception e){

            logger.log(Status.FAIL, "No se ha podido crear el tipo de UT con los valores por defecto");
            String screenshotPath = Utils.takeScreenshot(firefoxDriver);
            logger.fail(ExceptionUtils.getStackTrace(e), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            extent.flush();

            e.printStackTrace();
            return e.toString() + "\nERROR. A new UT type by default could not be created";
        }
    }

}
