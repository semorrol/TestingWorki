package UTs;

import UTType.NewUTTypeConLinea;
import Utils.*;
import Utils.TestOPasoPrevio;
import Workflow.NewWorkflowConLinea;
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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class NewUTYAbrir extends TestWithConfig {

    static final String TEST_ID = "newUTYAbrir";
    static final String TEST_NAME = "Crea una nueva UT y comprueba la funcionalidad de abrirla";

    NewWorkflowConLinea newWorkflowConLineaTest = new NewWorkflowConLinea(commonIni);
    NewUTTypeConLinea newUTTypeConLineaTest = new NewUTTypeConLinea(commonIni);

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    private HashMap<String, String> results = new HashMap<>();

    public NewUTYAbrir(Wini commonIni) {
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

            newUTTypeConLineaTest.check();
            newWorkflowConLineaTest.check();

            results.put("Creacion de UT  ->  ", newUT());

            return results;
        } catch (Exception e)
        {
            e.printStackTrace();
            return results;
        }
    }

    private String newUT() throws IOException, InterruptedException {

        try
        {
            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tn-menu/ul/li[5]/a")));
            WebElement nuevaUT = firefoxDriver.findElement(By.xpath("//tn-menu/ul/li[5]/a"));
            nuevaUT.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[span[contains(., 'Nueva Unidad de Trabajo (UT)')]]/following-sibling::div//input[@class = 'dx-texteditor-input']")));
            WebElement nombreUT = firefoxDriver.findElement(By.xpath("//div[span[contains(., 'Nueva Unidad de Trabajo (UT)')]]/following-sibling::div//input[@class = 'dx-texteditor-input']"));
            nombreUT.sendKeys("Unidad de trabajo selenium");

            WebElement lineaTrabajoSelector = firefoxDriver.findElement(By.xpath("/html/body/tn-root/tn-pages/p-dialog/div/div[2]/tn-nueva-ut-g/dx-validation-group/auto-template/div[2]/div/div[1]/dxt-selectbox/div/dx-select-box"));
            Actions actions = new Actions(firefoxDriver);
            actions.moveToElement(lineaTrabajoSelector).click().perform();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class = 'dx-item dx-list-item']//div[contains(., 'Linea de trabajo selenium con colaborador')]")));
            WebElement lineaDeTrabajo = firefoxDriver.findElement(By.xpath("//div[@class = 'dx-item dx-list-item']//div[contains(., 'Linea de trabajo selenium con colaborador')]"));
            lineaDeTrabajo.click();

            WebElement crearYAbrirButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Crear y abrir')]"));
            crearYAbrirButton.click();

            try
            {
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Ficha de UT')]")));
                Thread.sleep(2000);
            } catch (Exception e)
            {
                Report.testFailed(TEST_NAME, "No aparece la ficha de UT al pulsar en crear y abrir", ExceptionUtils.getStackTrace(e), firefoxDriver);

                e.printStackTrace();
                return e.toString() + "\nERROR. Se ha pulsado en crear y abrir pero no se abre la ficha de la UT";
            }

            Report.testPassed(TEST_ID, TEST_NAME, "Se ha creado una UT y se ha comprobado que se abre correctamente");

            return "Test OK. Se ha creado una unidad de trabajo (UT)";
        } catch (Exception e)
        {
            Report.testFailed(TEST_NAME, "No se ha podido crear la UT", ExceptionUtils.getStackTrace(e), firefoxDriver);

            e.printStackTrace();
            return e.toString() + "\nERROR. No se ha podido crear una unidad de trabajo (UT)";
        }
    }
}
