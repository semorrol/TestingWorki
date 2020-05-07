package UTType;

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

public class NewUTTypeConLinea extends TestWithConfig {

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

            results.put("Crea un nuevo tipo de ut asociandole una linea de trabajo  ->  ", newUTTypeConLinea());

            return  results;
        } catch (Exception e)
        {
            e.printStackTrace();
            return results;
        }
    }

    private String newUTTypeConLinea()
    {
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

            return "Test OK. Se ha creado un nuevo tipo de ut con linea de trabajo asociada";
        } catch (Exception e)
        {
            e.printStackTrace();
            return e.toString() + "\nERROR. No se ha podido crear un nuevo tipo de ut con linea de trabajo asociada";
        }
    }
}
