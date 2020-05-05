package UTType;

import Utils.MyFirefoxDriver;
import Utils.TestWithConfig;
import org.ini4j.Wini;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Utils.Utils;
import java.util.HashMap;
import java.util.List;

public class DeleteUTTypeCheckAlert extends TestWithConfig {

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    HashMap<String, String> results = new HashMap<>();

    public DeleteUTTypeCheckAlert(Wini commonIni) {
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

            results.put("Eliminacion de un tipo sin UT asociadas. Aparece antes un mensaje de confirmacion", deleteUTTypeCheckAlert());

            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return results;
        }

    }

    public String deleteUTTypeCheckAlert(){
        try {
            Utils.goToUTType(firefoxDriver, firefoxWaiting);

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class = 'tn-button--dropdown']")));
            WebElement thunderButton = firefoxDriver.findElement(By.xpath("//button[@class = 'tn-button--dropdown']"));
            thunderButton.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Eliminar')]")));
            WebElement deleteButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Eliminar')]"));
            deleteButton.click();

            try
            {
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Sí')]")));
                WebElement yesButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Sí')]"));
                yesButton.click();
            } catch(Exception e)
            {
                e.printStackTrace();
                return e.toString() + "\nERROR. No aparece el mensaje de confirmación de borrado de tipo";
            }

            return "Test OK. El borrado de tipos funciona correctamente";
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString() + "\nERROR. Excepcion inesperada";
        }
    }
}
