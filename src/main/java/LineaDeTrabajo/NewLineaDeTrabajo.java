package LineaDeTrabajo;

import Login.LoginWorki;
import Utils.MyFirefoxDriver;
import Utils.TestWithConfig;
import Utils.Utils;
import Utils.Report;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class NewLineaDeTrabajo extends TestWithConfig {

    static final String TEST_ID = "newLineaTrabajo";
    static final String TEST_NAME = "Crea una linea de trabajo vacia";

    LoginWorki loginWorkiTest = new LoginWorki(commonIni);

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    private HashMap<String, String> results = new HashMap<>();

    public NewLineaDeTrabajo(Wini commonIni) {
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

            loginWorkiTest.check();

            results.put("Crea una linea de trabajo", crearLineaTrabajo());

            return results;
        } catch (Exception e)
        {
            e.printStackTrace();
            return results;
        }
    }

    private String crearLineaTrabajo() throws IOException, InterruptedException {

        try
        {
            Utils.goToLineasDeTrabajo(firefoxDriver, firefoxWaiting);

            WebElement nuevaLinea = firefoxDriver.findElement(By.xpath("//span[contains(., 'Nueva Línea de trabajo')]"));
            Thread.sleep(400);
            nuevaLinea.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[span[contains(., 'Nueva línea de trabajo')]]/following-sibling::div//input[@class = 'dx-texteditor-input']")));
            WebElement nombreLinea = firefoxDriver.findElement(By.xpath("//div[span[contains(., 'Nueva línea de trabajo')]]/following-sibling::div//input[@class = 'dx-texteditor-input']"));
            nombreLinea.sendKeys("Linea de trabajo selenium");

            WebElement crearLinea = firefoxDriver.findElement(By.xpath("//span[contains(., 'Crear')]"));
            crearLinea.click();

            try
            {
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'Linea de trabajo selenium')]")));
            } catch (Exception e)
            {

                Report.testFailed(TEST_NAME, "La linea de trabajo no aparece en la tabla", ExceptionUtils.getStackTrace(e), firefoxDriver);

                e.printStackTrace();
                return e.toString() + "\nERROR. No se ha podido crear la linea de trabajo";
            }

            Report.testPassed(TEST_ID, TEST_NAME, "Se ha creado una linea de trabajo");

            return "Test OK. Se ha creado una linea de trabajo sin colaboradores ni workflow";
        } catch (Exception e)
        {
            Report.testFailed(TEST_NAME, "No se ha podido crear una linea de trabajo", ExceptionUtils.getStackTrace(e), firefoxDriver);

            e.printStackTrace();
            return e.toString() + "\nERROR. No se ha podido crear la linea de trabajo";
        }
    }
}
