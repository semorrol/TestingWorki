package UTs;

import Utils.MyFirefoxDriver;
import Utils.TestWithConfig;
import org.ini4j.Wini;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.List;

public class CheckValoresObligatorios extends TestWithConfig {

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

            results.put("Comprueba que no deja crear una unidad de trabajo sin poner todos los parametros obligatorios  ->  ", checkValoresObligatorios());

            return  results;

        } catch(Exception e)
        {
            e.printStackTrace();
            return results;
        }
    }

    private String checkValoresObligatorios()
    {
        try
        {
            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tn-menu/ul/li[4]/a")));
            WebElement nuevaUT = firefoxDriver.findElement(By.xpath("//tn-menu/ul/li[4]/a"));
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
                e.printStackTrace();
                return e.toString() + "\nERROR. No aparece la alerta que te indica que debes introducir un nombre para la unidad de trabajo";
            }

            return "Test OK. Al intentar crear una UT sin nombre aparece una alerta";

        } catch (Exception e)
        {
            e.printStackTrace();
            return e.toString() + "\nERROR. No se ha podido completar el test";
        }
    }
}
