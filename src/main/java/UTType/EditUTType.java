package UTType;

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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class EditUTType extends TestWithConfig {

    static final String TEST_ID = "editUTType";
    static final String TEST_NAME = "Edita un tipo de UT y comprueba que los valores se actualizan";

    NewUTTypeByDefault newUTTypeByDefaultTest = new NewUTTypeByDefault(commonIni);

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    HashMap<String, String> results = new HashMap<>();

    public EditUTType(Wini commonIni) {
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

        try{
            myFirefoxDriver = MyFirefoxDriver.getMyFirefoxDriver();
            firefoxDriver = myFirefoxDriver.getFirefoxDriver();
            firefoxWaiting = myFirefoxDriver.getFirefoxWaiting();

            newUTTypeByDefaultTest.check();

            results.put("-- Cambio de valores en formulario y su visualizacion en lista de tipos ademas de comprobar guardado al volver a acceder al tipo  ->  ", editUTTypeAndCheck());

            return results;
        } catch (Exception e){
            e.printStackTrace();
            return results;
        }
    }

    public String editUTTypeAndCheck() throws IOException, InterruptedException {

        try
        {
            Utils.goToUTType(firefoxDriver, firefoxWaiting);

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'UT type by default')]/preceding-sibling::td//button[@class = 'tn-button--dropdown']")));
            WebElement thunderButton = firefoxDriver.findElement(By.xpath("//td[contains(., 'UT type by default')]/preceding-sibling::td//button[@class = 'tn-button--dropdown']"));
            thunderButton.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'UT type by default')]/preceding-sibling::td//span[contains(., 'Editar')]")));
            WebElement editButton = firefoxDriver.findElement(By.xpath("//td[contains(., 'UT type by default')]/preceding-sibling::td//span[contains(., 'Editar')]"));
            editButton.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Fallo')]")));
            WebElement failureCheckbox = firefoxDriver.findElement(By.xpath("//span[contains(., 'Fallo')]"));
            failureCheckbox.click();

            WebElement saveButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Guardar')]"));
            saveButton.click();

            try
            {
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'UT type by default')]/following-sibling::td//input[@type = 'hidden' and @value = 'true']")));
            } catch(Exception e)
            {
                Report.testFailed(TEST_NAME, "El tipo de UT ha sido editado pero no se actualiza correctamente en la tabla", ExceptionUtils.getStackTrace(e), firefoxDriver);

                e.printStackTrace();
                return e.toString() + "\nERROR. El tipo de UT ha sido editado pero no se actualiza correctamente en la tabla";
            }

            //Volvemos a acceder al formulario para comprobar que se ha editado correctamente
            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'UT type by default')]/preceding-sibling::td//button[@class = 'tn-button--dropdown']")));
            thunderButton = firefoxDriver.findElement(By.xpath("//td[contains(., 'UT type by default')]/preceding-sibling::td//button[@class = 'tn-button--dropdown']"));
            thunderButton.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'UT type by default')]/preceding-sibling::td//span[contains(., 'Editar')]")));
            editButton = firefoxDriver.findElement(By.xpath("//td[contains(., 'UT type by default')]/preceding-sibling::td//span[contains(., 'Editar')]"));
            editButton.click();

            try {
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Fallo')]//ancestor::dx-check-box[@aria-checked = 'true']")));
            } catch (Exception e){
                Report.testFailed(TEST_NAME, "El cambio realizado no aparece al volver a entrar al formulario del tipo de UT", ExceptionUtils.getStackTrace(e),
                        firefoxDriver);

                e.printStackTrace();
                return e.toString() + "\nERROR. El cambio realizado no aparece al volver a entrar al formulario del tipo de UT";
            }

            Report.testPassed(TEST_ID, TEST_NAME, "Se ha editado el tipo de UT y se ha comprobado que cambia en la tabla y en el formulario");

            return "Test OK. Se ha editado el tipo de UT y se ha comprobado que cambia en la tabla y en el formulario";
        } catch(Exception e)
        {
            Report.testFailed(TEST_NAME, "No se ha podido completar la prueba", ExceptionUtils.getStackTrace(e), firefoxDriver);

            e.printStackTrace();
            return e.toString() + "\n ERROR. Excepcion inesperada";
        }
    }
}
