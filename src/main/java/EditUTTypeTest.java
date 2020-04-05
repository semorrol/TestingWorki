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

public class EditUTTypeTest extends TestWithConfig {

    static String url;
    static String headless;
    static String username;
    static String password;
    static String imagePath;

    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    HashMap<String, String> results = new HashMap<>();

    public EditUTTypeTest(Wini commonIni) {
        super(commonIni);
    }

    @Override
    public HashMap<String, List<String>> getRequiredParameters() {
        HashMap<String, List<String>> requiredParameters = new HashMap<>();

        requiredParameters.put("General", new ArrayList<>(Arrays.asList("url")));
        requiredParameters.put("Login", new ArrayList<>(Arrays.asList("username", "password")));
        requiredParameters.put("Paths", new ArrayList<>(Arrays.asList("imagePath")));

        return requiredParameters;
    }

    @Override
    public HashMap<String, String> check() throws Exception {
        super.checkParameters();

        url = commonIni.get("General", "url");
        headless = commonIni.get("General", "headless");
        username = commonIni.get("Login", "username");
        password = commonIni.get("Login", "password");
        imagePath = commonIni.get("Paths", "imagePath");

        try{
            firefoxDriver = DriversConfig.headlessOrNot(headless);
            firefoxWaiting = new WebDriverWait(firefoxDriver, 5);

            results.put(" Edit default UT type adding failure  ->  ", editDefaultUTType());
            results.put(" Edit default UT type adding a image  ->  ", editDefaultUTTypeAddingImage());


            return results;
        } catch(Exception e){
            e.printStackTrace();
            return results;
        } finally {
            firefoxDriver.close();
        }
    }

    public String editDefaultUTType(){
        try{
            Utils.loginWorki(firefoxDriver, firefoxWaiting, url, username, password);
        } catch (Exception e){
            e.printStackTrace();
            return e.toString() + "\nERROR. The login on Worki failed";
        }

        try{

            Utils.goToUTType(firefoxDriver, firefoxWaiting);

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'UT type by default')]/preceding-sibling::td//button[@class = 'tn-button--dropdown']")));
            WebElement thunderButton = firefoxDriver.findElement(By.xpath("//td[contains(., 'UT type by default')]/preceding-sibling::td//button[@class = 'tn-button--dropdown']"));
            thunderButton.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Editar')]")));
            WebElement editButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Editar')]"));
            editButton.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Fallo')]")));
            WebElement failureCheckbox = firefoxDriver.findElement(By.xpath("//span[contains(., 'Fallo')]"));
            failureCheckbox.click();

            WebElement saveButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Guardar')]"));
            saveButton.click();

            try{
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'UT type by default')]/following-sibling::td//input[@type = 'hidden' and @value = 'true']")));
            } catch(Exception e) {
                e.printStackTrace();
                return e.toString() + "\nERROR. The UT type was edited but never changed";
            }

            return "Test OK. The UT type by default was edited with failure activated";
        } catch(Exception e){
            e.printStackTrace();
            return e.toString() + "\nERROR.";
        }
    }

    public String editDefaultUTTypeAddingImage(){
        try{
            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'UT type by default')]/preceding-sibling::td//button[@class = 'tn-button--dropdown']")));
            WebElement thunderButton = firefoxDriver.findElement(By.xpath("//td[contains(., 'UT type by default')]/preceding-sibling::td//button[@class = 'tn-button--dropdown']"));
            thunderButton.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Editar')]")));
            WebElement editButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Editar')]"));
            editButton.click();

            WebElement uploadImageButton = firefoxDriver.findElement(By.xpath("//input[@class = 'dx-fileuploader-input']"));
            uploadImageButton.sendKeys(imagePath);

            WebElement saveButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Guardar')]"));
            saveButton.click();

            try{
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[contains(@src, 'data') and @title = 'UT type by default']")));
            } catch (Exception e){
                e.printStackTrace();
                return e.toString() + "\nERROR. The image was uploaded but did not updated correctly";
            }

            return "Test OK. The UT type by default was edited adding an image";
        } catch(Exception e){
            e.printStackTrace();
            return e.toString() + "\nERROR. The UT type could not be edited";
        }

    }
}
