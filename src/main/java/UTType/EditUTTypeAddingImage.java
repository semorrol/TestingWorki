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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EditUTTypeAddingImage extends TestWithConfig {

    private String imagePath;

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    HashMap<String, String> results = new HashMap<>();

    public EditUTTypeAddingImage(Wini commonIni) {
        super(commonIni);
    }

    @Override
    public HashMap<String, List<String>> getRequiredParameters() {
        HashMap<String, List<String>> requiredParameters = new HashMap<>();
        requiredParameters.put("Paths", new ArrayList<>(Arrays.asList("imagePath")));

        return requiredParameters;
    }

    @Override
    public HashMap<String, String> check() throws Exception {
        super.checkParameters();

        imagePath = commonIni.get("Paths", "imagePath");

        try
        {
            myFirefoxDriver = MyFirefoxDriver.getMyFirefoxDriver();
            firefoxDriver = myFirefoxDriver.getFirefoxDriver();
            firefoxWaiting = myFirefoxDriver.getFirefoxWaiting();

            results.put("Al cambiar la imagen del tipo esta se muestra en la lista", editUTTypeAddingImage());

            return results;
        } catch (Exception e)
        {
            e.printStackTrace();
            return results;
        }
    }

    private String editUTTypeAddingImage()
    {
        try{
            Utils.goToUTType(firefoxDriver, firefoxWaiting);

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
                return e.toString() + "\nERROR. La imagen se subió pero no aparece en la lista";
            }

            return "Test OK. Se ha añadido una imagen al tipo de UT por defecto";
        } catch(Exception e){
            e.printStackTrace();
            return e.toString() + "\nERROR. Error inesperado. No se ha podido editar el tipo de ut";
        }

    }
}
