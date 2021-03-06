package UTType;

import Login.LoginWorki;
import Utils.DriversConfig;
import Utils.TestWithConfig;
import Utils.Utils;
import Utils.TestOPasoPrevio;
import Utils.MyFirefoxDriver;
import Utils.Report;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.sun.org.apache.bcel.internal.ExceptionConst;
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

public class NewUTTypeWithFailure extends TestWithConfig {

    static final String TEST_ID = "newUTTypeWithFailure";
    static final String TEST_NAME = "Crea un nuevo tipo de UT con fallo";

    LoginWorki loginWorkiTest = new LoginWorki(commonIni);

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    HashMap<String, String> results = new HashMap<>();

    public NewUTTypeWithFailure(Wini commonIni) {
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

            results.put(" Creates a new UT Type with failure  ->  ", newUTTYpeWithFailure());

            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return results;
        }
    }
    public String newUTTYpeWithFailure() throws IOException, InterruptedException {

        try{
            Utils.goToUTType(firefoxDriver, firefoxWaiting);

            WebElement newUTTypeButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Nuevo Tipo de UT')]"));
            newUTTypeButton.click();

            //Espera hasta que se muestre el input del nombre del nuevo tipo de ut
            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@class = 'dx-texteditor-input']")));

            WebElement UTTypeNameInput = firefoxDriver.findElement(By.xpath("//dx-validation-group//input[@class = 'dx-texteditor-input']"));
            UTTypeNameInput.sendKeys("UT type with failure");

            WebElement failureCheckbox = firefoxDriver.findElement(By.xpath("//span[contains(., 'Fallo')]"));
            failureCheckbox.click();

            WebElement createButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Crear')]"));
            createButton.click();

            //Comprueba que se ha creado el tipo de ut con fallo
            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'UT type with failure')]/following-sibling::td//input[@type = 'hidden' and @value = 'true']")));

            Report.testPassed(TEST_ID, TEST_NAME, "Se ha creado un nuevo tipo de UT con fallo");

            return "Test OK. Se ha creado un nuevo tipo de UT con fallo";
        } catch (Exception e) {
            Report.testFailed(TEST_NAME,"No se ha podido crear un nuevo tipo de ut con fallo", ExceptionUtils.getStackTrace(e), firefoxDriver);

            e.printStackTrace();
            return e.toString() + "\nERROR. No se ha podido crear un nuevo tipo de ut con fallo";
        }
    }

}
