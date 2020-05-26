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
import Utils.MyFirefoxDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CheckUTTYpeNameAlreadyExists extends TestWithConfig {

    Report myReport;
    ExtentHtmlReporter reporter;
    ExtentReports extent;

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    HashMap<String, String> results = new HashMap<>();

    public CheckUTTYpeNameAlreadyExists(Wini commonIni) {
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

            results.put("Comprueba que no deja crear un tipo de UT con el mismo nombre de una que ya existe", checkUTTypeAlreadyExists());

            return results;
        } catch (Exception e)
        {
            e.printStackTrace();
            return results;
        }
    }

    public String checkUTTypeAlreadyExists() throws IOException, InterruptedException {

        myReport = Report.getMyReporter();
        reporter = myReport.getExtentHtmlReporter();
        extent = myReport.getExtentReports();

        ExtentTest logger = extent.createTest("Comprueba que un tipo de UT ya existe al intentar crearla con el mismo nombre");

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

        try{
            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(., 'Ya existe un tipo con ese nombre')]")));
        } catch(Exception e){

            logger.log(Status.FAIL, "No aparece la alerta de que ya existe un tipo con el mismo nombre");
            String screenshotPath = Utils.takeScreenshot(firefoxDriver);
            logger.fail(ExceptionUtils.getStackTrace(e), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            extent.flush();

            e.printStackTrace();
            return e.toString() + "ERROR. Se permite crear un tipo de UT con el mismo nombre que otro existente";
        }

        logger.log(Status.PASS, "No deja crear un tipo de ut con el mismo nombre que otro existente");
        extent.flush();

        return "Test OK. No deja crear un tipo con el mismo nombre que otro existente";
    }
}
