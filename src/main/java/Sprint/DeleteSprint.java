package Sprint;

import Utils.MyFirefoxDriver;
import Utils.TestWithConfig;
import Utils.Utils;
import org.ini4j.Wini;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.List;

public class DeleteSprint extends TestWithConfig {

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    private HashMap<String, String> results = new HashMap<>();

    public DeleteSprint(Wini commonIni) {
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

            results.put("Borra un sprint  ->  ", deleteSprint());

            return results;

        } catch (Exception e)
        {
            e.printStackTrace();
            return results;
        }
    }

    private String deleteSprint()
    {
        try
        {
            Utils.goToSprints(firefoxDriver, firefoxWaiting);

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

            try
            {
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'Sprint con valores obligatorios')]")));
                //si encuentra el elemento es que no se ha borrado correctamente
                return "ERROR. Se ha borrado el sprint pero sigue apareciendo en la tabla";
            } catch (Exception e)
            { }

            return "Test OK. Se ha borrado el sprint correctamente";
        } catch (Exception e)
        {
            e.printStackTrace();
            return e.toString() + "\nERROR. No se ha podido borrar el sprint";
        }
    }
}
