package UTType;

import Utils.DriversConfig;
import Utils.TestWithConfig;
import Utils.Utils;
import org.ini4j.Wini;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NewUTTypeNonActive extends TestWithConfig {
    static String url;
    static String headless;
    static String username;
    static String password;

    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    HashMap<String, String> results = new HashMap<>();

    public NewUTTypeNonActive(Wini commonIni) {
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

        url = commonIni.get("General", "url");
        headless = commonIni.get("General", "headless");
        username = commonIni.get("Login", "username");
        password = commonIni.get("Login", "password");

        try {
            firefoxDriver = DriversConfig.headlessOrNot(headless);
            firefoxWaiting = new WebDriverWait(firefoxDriver, 5);

            results.put(" Creates a new UT Type non active  ->  ", newUTTypenonActive());

            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return results;
        } finally {
            firefoxDriver.close();
        }

    }

    public String newUTTypenonActive(){
        try{
            Utils.goToUTType(firefoxDriver, firefoxWaiting);

            WebElement newUTTypeButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Nuevo Tipo de UT')]"));
            newUTTypeButton.click();

            //Espera hasta que se muestre el input del nombre del nuevo tipo de ut
            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@class = 'dx-texteditor-input']")));

            WebElement UTTypeNameInput = firefoxDriver.findElement(By.xpath("//dx-validation-group//input[@class = 'dx-texteditor-input']"));
            UTTypeNameInput.sendKeys("UT type non active");

            WebElement activeCheckbox = firefoxDriver.findElement(By.xpath("//span[contains(., 'Activo')]/preceding-sibling::span"));
            activeCheckbox.click();

            WebElement createButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Crear')]"));
            createButton.click();

            //Comprueba que se ha creado y que no aparece en los activos pero si en los no activos
            try{
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'UT type non active')]")));
                return "ERROR. The non active UT type appears on the actives UT types view";
            } catch (Exception e) {
                Utils.showNonActiveUTTypes(firefoxDriver, firefoxWaiting);

                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'UT type non active')]")));
            }

            return "Test OK. A new UT type non active was created";
        } catch(Exception e) {
            e.printStackTrace();
            return e.toString() + "\nERROR. A new UT type non active could not be created";
        }
    }
}