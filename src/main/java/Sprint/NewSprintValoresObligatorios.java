package Sprint;

import LineaDeTrabajo.NewLineaConColaborador;
import Utils.MyFirefoxDriver;
import Utils.TestWithConfig;
import Utils.Utils;
import Utils.Report;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.sun.jdi.event.ExceptionEvent;
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

public class NewSprintValoresObligatorios extends TestWithConfig {

    NewLineaConColaborador newLineaConColaboradorTest = new NewLineaConColaborador(commonIni);

    Report myReport;
    ExtentHtmlReporter reporter;
    ExtentReports extent;


    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    private HashMap<String, String> results = new HashMap<>();

    public NewSprintValoresObligatorios(Wini commonIni) {
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

            newLineaConColaboradorTest.check();

            results.put("Crea un nuevo Sprint con los valores obligatorios  ->  ", newSprint());

            return results;
        } catch (Exception e)
        {
            e.printStackTrace();
            return results;
        }
    }


    private String newSprint() throws IOException, InterruptedException {

        myReport = Report.getMyReporter();
        reporter = myReport.getExtentHtmlReporter();
        extent = myReport.getExtentReports();

        ExtentTest logger = extent.createTest("Crea un sprint con los valores obligatorios necesarios");

        try
        {
            Utils.goToSprints(firefoxDriver, firefoxWaiting);

            WebElement nuevoSprint = firefoxDriver.findElement(By.xpath("//span[contains(., 'Nuevo Sprint')]"));
            nuevoSprint.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[span[contains(., 'Nuevo Sprint')]]/following-sibling::div//input[@class = 'dx-texteditor-input']")));

            WebElement nombreInput = firefoxDriver.findElement(By.xpath("/html/body/tn-root/tn-pages/tn-modal/p-dialog/div/div[2]/dx-validation-group/auto-template/div[1]/div/div/dxt-textbox/div/dx-text-box/div/div[1]/input"));
            nombreInput.sendKeys("Sprint con valores obligatorios");

            WebElement inicioEstimadoInput = firefoxDriver.findElement(By.xpath("/html/body/tn-root/tn-pages/tn-modal/p-dialog/div/div[2]/dx-validation-group/auto-template/div[2]/div/div[1]/dxt-datebox/div/dx-date-box/div[1]/div/div[1]/input"));
            inicioEstimadoInput.sendKeys("07/05/2020");

            WebElement finEstimadoInput = firefoxDriver.findElement(By.xpath("/html/body/tn-root/tn-pages/tn-modal/p-dialog/div/div[2]/dx-validation-group/auto-template/div[2]/div/div[2]/dxt-datebox/div/dx-date-box/div/div/div[1]/input"));
            finEstimadoInput.sendKeys("07/09/2020");

            WebElement crearButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Crear')]"));
            crearButton.click();

            try
            {
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'Sprint con valores obligatorios')]")));
            } catch (Exception e)
            {
                logger.log(Status.FAIL, "Se ha rellenado el formulario y se ha creado el sprint pero no aparece en la tabla de sprints");
                String screenshotPath = Utils.takeScreenshot(firefoxDriver);
                logger.fail(ExceptionUtils.getStackTrace(e), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                extent.flush();


                e.printStackTrace();
                return e.toString() + "\nERROR. Se ha rellenado el formulario y se ha creado el sprint pero no aparece en la tabla de sprints";
            }

            logger.log(Status.PASS, "Se ha creado un sprint con los valores obligatorios");
            extent.flush();

            return "Test OK. Se ha creado un sprint con los valores minimos para proceder";
        } catch (Exception e)
        {

            logger.log(Status.FAIL, "No se ha podido crear el sprint con los valores obligatorios");
            String screenshotPath = Utils.takeScreenshot(firefoxDriver);
            logger.fail(ExceptionUtils.getStackTrace(e), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            extent.flush();

            e.printStackTrace();
            return e.toString() + "\nERROR. No se ha podido crear el sprint";
        }
    }
}
