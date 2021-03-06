package UTType;

import LineaDeTrabajo.NewLineaConColaborador;
import Login.LoginWorki;
import Utils.MyFirefoxDriver;
import Utils.TestWithConfig;
import Utils.TestOPasoPrevio;
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

public class NewUTTypeConLinea extends TestWithConfig {

    static final String TEST_ID = "newUTTypeConLinea";
    static final String TEST_NAME = "Crea un nuevo tipo de UT asociandole una linea de trabajo";

    NewLineaConColaborador newLineaConColaboradorTest = new NewLineaConColaborador(commonIni);

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    private HashMap<String, String> results = new HashMap<>();

    public NewUTTypeConLinea(Wini commonIni) {
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

            results.put("Crea un nuevo tipo de ut asociandole una linea de trabajo  ->  ", newUTTypeConLinea());

            return  results;
        } catch (Exception e)
        {
            e.printStackTrace();
            return results;
        }
    }

    private String newUTTypeConLinea() throws IOException, InterruptedException {

        try
        {
            Utils.goToUTType(firefoxDriver, firefoxWaiting);

            WebElement newUTTypeButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Nuevo Tipo de UT')]"));
            newUTTypeButton.click();

            //Espera hasta que se muestre el input del nombre del nuevo tipo de ut
            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@class = 'dx-texteditor-input']")));

            WebElement UTTypeNameInput = firefoxDriver.findElement(By.xpath("//dx-validation-group//input[@class = 'dx-texteditor-input']"));
            UTTypeNameInput.sendKeys("Tipo de UT con linea");

            WebElement lineaDeTrabajo = firefoxDriver.findElement(By.xpath("//div[@class = 'dx-template-wrapper dx-item-content dx-list-item-content ng-star-inserted']//div[contains(., 'Linea de trabajo selenium con colaborador')]"));
            lineaDeTrabajo.click();

            WebElement createButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Crear')]"));
            createButton.click();

            //Comprueba que se ha creado el tipo de ut
            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'Tipo de UT con linea')]")));

            Report.testPassed(TEST_ID, TEST_NAME, "Se ha creado un tipo de ut con linea de trabajo asociada");

            return "Test OK. Se ha creado un nuevo tipo de ut con linea de trabajo asociada";
        } catch (Exception e)
        {
            Report.testFailed(TEST_NAME,"No se ha podido crear un nuevo tipo de ut con linea de trabajo asociada", ExceptionUtils.getStackTrace(e), firefoxDriver);

            e.printStackTrace();
            return e.toString() + "\nERROR. No se ha podido crear un nuevo tipo de ut con linea de trabajo asociada";
        }
    }
}
