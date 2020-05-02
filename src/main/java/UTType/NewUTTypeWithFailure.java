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

public class NewUTTypeWithFailure extends TestWithConfig {
    static String url;
    static String headless;
    static String username;
    static String password;

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

        url = commonIni.get("General", "url");
        headless = commonIni.get("General", "headless");
        username = commonIni.get("Login", "username");
        password = commonIni.get("Login", "password");

        try {
            firefoxDriver = DriversConfig.headlessOrNot(headless);
            firefoxWaiting = new WebDriverWait(firefoxDriver, 5);

            results.put(" Creates a new UT Type with failure  ->  ", newUTTYpeWithFailure());

            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return results;
        } finally {
            firefoxDriver.close();
        }



    }
    public String newUTTYpeWithFailure(){
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

            return "Test OK. A new UT type with failure was created";
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString() + "\nERROR. A new UT type with failure could not be created";
        }
    }

}
