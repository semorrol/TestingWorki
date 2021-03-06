package UTs;

import Login.LoginWorki;
import Utils.MyFirefoxDriver;
import Utils.TestWithConfig;
import Utils.Report;
import Utils.Utils;
import Utils.TestOPasoPrevio;
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
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CheckValoresObligatorios extends TestWithConfig {

    static final String TEST_ID = "checkValoresObligatorios";
    static  final String TEST_NAME = "Comprueba que aparece la alerta de rellenar todos los campos obligatorios";

    LoginWorki loginWorkiTest = new LoginWorki(commonIni);

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    private HashMap<String, String> results = new HashMap<>();

    public CheckValoresObligatorios(Wini commonIni) {
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

            loginWorkiTest.check();

            results.put("Comprueba que no deja crear una unidad de trabajo sin poner todos los parametros obligatorios  ->  ", checkValoresObligatorios());

            return  results;

        } catch(Exception e)
        {
            e.printStackTrace();
            return results;
        }
    }

    private String checkValoresObligatorios() throws IOException, InterruptedException {

        try
        {
            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tn-menu/ul/li[5]/a")));
            WebElement nuevaUT = firefoxDriver.findElement(By.xpath("//tn-menu/ul/li[5]/a"));
            nuevaUT.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[span[contains(., 'Nueva Unidad de Trabajo (UT)')]]/following-sibling::div//input[@class = 'dx-texteditor-input']")));

            WebElement crearYAbrirButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Crear y abrir')]"));
            crearYAbrirButton.click();

            try
            {
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[span[contains(., 'Nueva Unidad de Trabajo (UT)')]]/following-sibling::div//input[@class = 'dx-texteditor-input' " +
                        "and @aria-invalid = 'true']")));
            } catch (Exception e)
            {
                Report.testFailed(TEST_NAME, "No aparece la alerta que te indica que debes introducir un nombre para la unidad de trabajo", ExceptionUtils.getStackTrace(e),
                        firefoxDriver);

                e.printStackTrace();
                return e.toString() + "\nERROR. No aparece la alerta que te indica que debes introducir un nombre para la unidad de trabajo";
            }

            Report.testPassed(TEST_ID, TEST_NAME, "La alerta de rellenar todos los campos aparece");

            return "Test OK. Al intentar crear una UT sin nombre aparece una alerta";

        } catch (Exception e)
        {

            Report.testFailed(TEST_NAME, "Ha habido un error al interaccionar con la web", ExceptionUtils.getStackTrace(e), firefoxDriver);

            e.printStackTrace();
            return e.toString() + "\nERROR. No se ha podido completar el test";
        }
    }
}
