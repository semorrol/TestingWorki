package UTs;

import Utils.MyFirefoxDriver;
import Utils.TestWithConfig;
import Utils.Report;
import Utils.Utils;
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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class NewUTyNueva extends TestWithConfig {

    Report myReport;
    ExtentHtmlReporter reporter;
    ExtentReports extent;

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    private HashMap<String, String> results = new HashMap<>();

    public NewUTyNueva(Wini commonIni) {
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

            results.put("Crea una nueva UT y pulsa en crear otra  ->  ", newUT());

            return results;
        } catch (Exception e)
        {
            e.printStackTrace();
            return results;
        }
    }

    private String newUT() throws IOException, InterruptedException {

        myReport = Report.getMyReporter();
        reporter = myReport.getExtentHtmlReporter();
        extent = myReport.getExtentReports();


        ExtentTest logger = extent.createTest("Crea una nueva UT y pulsa en Crear y Nueva para comprobar que aparece el formulario de nuevo");

        try
        {
            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tn-menu/ul/li[4]/a")));
            WebElement nuevaUT = firefoxDriver.findElement(By.xpath("//tn-menu/ul/li[4]/a"));
            nuevaUT.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[span[contains(., 'Nueva Unidad de Trabajo (UT)')]]/following-sibling::div//input[@class = 'dx-texteditor-input']")));
            WebElement nombreUT = firefoxDriver.findElement(By.xpath("//div[span[contains(., 'Nueva Unidad de Trabajo (UT)')]]/following-sibling::div//input[@class = 'dx-texteditor-input']"));
            nombreUT.sendKeys("Unidad de trabajo");

            WebElement lineaTrabajoSelector = firefoxDriver.findElement(By.xpath("/html/body/tn-root/tn-pages/p-dialog/div/div[2]/tn-nueva-ut-g/dx-validation-group/auto-template/div[2]/div/div[1]/dxt-selectbox/div/dx-select-box"));
            Actions actions = new Actions(firefoxDriver);
            actions.moveToElement(lineaTrabajoSelector).click().perform();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class = 'dx-item dx-list-item']//div[contains(., 'Linea de trabajo selenium con colaborador')]")));
            WebElement lineaDeTrabajo = firefoxDriver.findElement(By.xpath("//div[@class = 'dx-item dx-list-item']//div[contains(., 'Linea de trabajo selenium con colaborador')]"));
            lineaDeTrabajo.click();

            WebElement crearYAbrirButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Crear y nueva')]"));
            crearYAbrirButton.click();

            try
            {
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Nueva Unidad de Trabajo (UT)')]")));
            } catch (Exception e)
            {
                logger.log(Status.FAIL, "Se ha pulsado en crear y abrir pero no se abre el formulario para crear otra UT");
                String screenshotPath = Utils.takeScreenshot(firefoxDriver);
                logger.fail(ExceptionUtils.getStackTrace(e), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                extent.flush();


                e.printStackTrace();
                return e.toString() + "\nERROR. Se ha pulsado en crear y abrir pero no se abre el formulario de crear otra";
            }

            logger.log(Status.PASS, "Se ha creado la UT y se ha comprobado que se abre el formulario al pulsar en crear y abrir");
            extent.flush();

            return "Test OK. Se ha creado una unidad de trabajo (UT) y se ha comprobado que se abre el formulario al pulsar en crear y nueva";
        } catch (Exception e)
        {
            logger.log(Status.FAIL, "No se ha crear la unidad de trabajo");
            String screenshotPath = Utils.takeScreenshot(firefoxDriver);
            logger.fail(ExceptionUtils.getStackTrace(e), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            extent.flush();


            e.printStackTrace();
            return e.toString() + "\nERROR. No se ha podido crear la unidad de trabajo";
        }
    }
}
