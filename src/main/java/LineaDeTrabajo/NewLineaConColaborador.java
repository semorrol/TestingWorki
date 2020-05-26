package LineaDeTrabajo;

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

public class NewLineaConColaborador extends TestWithConfig {

    Report myReport;
    ExtentHtmlReporter reporter;
    ExtentReports extent;


    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    private HashMap<String, String> results = new HashMap<>();

    public NewLineaConColaborador(Wini commonIni) {
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

            results.put("Crea una linea de trabajo con colaborador asociado", crearLineaTrabajoConColaborador());

            return results;
        } catch (Exception e)
        {
            e.printStackTrace();
            return results;
        }
    }

    private String crearLineaTrabajoConColaborador() throws IOException, InterruptedException {

        myReport = Report.getMyReporter();
        reporter = myReport.getExtentHtmlReporter();
        extent = myReport.getExtentReports();


        ExtentTest logger = extent.createTest("Crea una linea de trabajo asociandole colaborador");


        try
        {
            Utils.goToLineasDeTrabajo(firefoxDriver, firefoxWaiting);

            WebElement nuevaLinea = firefoxDriver.findElement(By.xpath("//span[contains(., 'Nueva Línea de trabajo')]"));
            nuevaLinea.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[span[contains(., 'Nueva línea de trabajo')]]/following-sibling::div//input[@class = 'dx-texteditor-input']")));
            WebElement nombreLinea = firefoxDriver.findElement(By.xpath("//div[span[contains(., 'Nueva línea de trabajo')]]/following-sibling::div//input[@class = 'dx-texteditor-input']"));
            nombreLinea.sendKeys("Linea de trabajo selenium con colaborador");

            WebElement colaborador = firefoxDriver.findElement(By.xpath("//div[@class = 'dx-scrollview-content']//div[contains(., 'Sebastián Morcillo')]"));
            colaborador.click();

            WebElement crearLinea = firefoxDriver.findElement(By.xpath("//span[contains(., 'Crear')]"));
            crearLinea.click();

            try {
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'Linea de trabajo selenium con colaborador')]")));
            } catch (Exception e)
            {
                logger.log(Status.FAIL, "La linea de trabajo no aparece en la tabla");
                String screenshotPath = Utils.takeScreenshot(firefoxDriver);
                logger.fail(ExceptionUtils.getStackTrace(e), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                extent.flush();


                e.printStackTrace();
                return e.toString() + "\nERROR. No se ha podido crear una linea de trabajo con colaborador";
            }

            logger.log(Status.PASS, "Se ha creado una linea de trabajo con colaborador");
            extent.flush();

            return "Test OK. Se ha creado una linea de trabajo con colaborador";
        } catch (Exception e)
        {

            logger.log(Status.FAIL, "No se ha podido crear una linea de trabajo con colaborador");
            String screenshotPath = Utils.takeScreenshot(firefoxDriver);
            logger.fail(ExceptionUtils.getStackTrace(e), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            extent.flush();

            e.printStackTrace();
            return e.toString() + "\nERROR. No se ha podido crear la linea de trabajo";
        }
    }
}
