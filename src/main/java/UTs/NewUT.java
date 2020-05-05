package UTs;

import Utils.MyFirefoxDriver;
import Utils.Test;
import Utils.TestWithConfig;
import org.ini4j.Wini;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.List;

public class NewUT extends TestWithConfig {

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    private HashMap<String, String> results = new HashMap<>();

    public NewUT(Wini commonIni) {
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

            results.put("Creacion de UT  ->  ", newUT());

            return results;
        } catch (Exception e)
        {
            e.printStackTrace();
            return results;
        }
    }

    private String newUT()
    {
        try
        {
            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tn-menu/ul/li[4]/a")));
            WebElement nuevaUT = firefoxDriver.findElement(By.xpath("//tn-menu/ul/li[4]/a"));
            nuevaUT.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[span[contains(., 'Nueva Unidad de Trabajo (UT)')]]/following-sibling::div//input[@class = 'dx-texteditor-input']")));
            WebElement nombreUT = firefoxDriver.findElement(By.xpath("//div[span[contains(., 'Nueva Unidad de Trabajo (UT)')]]/following-sibling::div//input[@class = 'dx-texteditor-input']"));
            nombreUT.sendKeys("Unidad de trabajo selenium");

            /*WebElement lineaTrabajoSelector = firefoxDriver.findElement(By.xpath("//div[div[contains(., 'Línea de trabajo')]]/following-sibling::dx-select-box"));
            Thread.sleep(500);
            lineaTrabajoSelector.click();*/

            Actions act =  new Actions(firefoxDriver);
            act.moveToElement(firefoxDriver.findElement(By.xpath("//div[div[contains(., 'Línea de trabajo')]]/following-sibling::dx-select-box//div[@class = 'dx-dropdowneditor-icon']"))).click().perform();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(., 'Linea de trabajo selenium con colaborador')]")));
            WebElement lineaDeTrabajo = firefoxDriver.findElement(By.xpath("//div[contains(., 'Linea de trabajo selenium con colaborador')]"));
            lineaDeTrabajo.click();


            return "Test OK. Se ha creado una unidad de trabajo (UT)";
        } catch (Exception e)
        {
            e.printStackTrace();
            return e.toString() + "\nERROR. No se ha podido crear una unidad de trabajo (UT)";
        }
    }
}
