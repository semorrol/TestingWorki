package Utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static void loginWorki(WebDriver driver, WebDriverWait waiting, String url, String username, String password) throws InterruptedException {
        driver.get(url);

        Thread.sleep(2000);

        //Comprobamos que la sesion no esta iniciada ya
        if(driver.findElements(By.xpath("//img[@class = 'topbar-logo']")).size() == 0)
        {
            waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//dx-text-box[@placeholder = 'Email']")));
            WebElement emailInput = driver.findElement(By.xpath("//dx-text-box[@placeholder = 'Email']//input"));
            emailInput.sendKeys(username);

            WebElement passwordInput = driver.findElement(By.xpath("//dx-text-box[@placeholder = 'Password']//input"));
            passwordInput.sendKeys(password);

            WebElement loginButton = driver.findElement(By.xpath("//button[@class = 'btn btn-primary btn-block btn-flat']"));
            loginButton.click();

            waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/tn-root/tn-login/div/div/dx-validation-group/div[3]/div/dx-select-box")));
            WebElement siteSelector = driver.findElement(By.xpath("/html/body/tn-root/tn-login/div/div/dx-validation-group/div[3]/div/dx-select-box"));
            siteSelector.click();

            waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(., 'Automatización Worki sitio restaurable')]")));
            WebElement site = driver.findElement(By.xpath("/html/body/div/div/div/div/div[1]/div/div[1]/div[2]/div[2]/div"));
            site.click();

            loginButton = driver.findElement(By.xpath("//button[@class = 'btn btn-primary btn-block btn-flat']"));
            loginButton.click();

            //Esperamos hasta que se muestre el logo de la aplicacion para comprobar que se ha logeado correctamente
            waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@class = 'topbar-logo']")));
        }

    }

    public static void goToUTType(WebDriver driver, WebDriverWait waiting) {
        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tn-menu/ul/li[11]/a")));
        WebElement siteElementsButton = driver.findElement(By.xpath("//tn-menu/ul/li[11]/a"));
        siteElementsButton.click();

        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Tipos de UT')]")));
        WebElement UTTypesButton = driver.findElement(By.xpath("//span[contains(., 'Tipos de UT')]"));
        UTTypesButton.click();

        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Nuevo Tipo de UT')]")));
    }

    public static void showNonActiveUTTypes(WebDriver driver, WebDriverWait waiting) throws InterruptedException {
        Thread.sleep(500);
        WebElement filterButton = driver.findElement(By.xpath("//div[@class = 'dx-column-indicators']/span[@class = 'dx-header-filter']"));
        filterButton.click();

        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(., 'Verdadero')]/preceding-sibling::div//span[@class = 'dx-checkbox-icon']")));
        WebElement activeFilterCheckBox = driver.findElement(By.xpath("//div[contains(., 'Verdadero')]/preceding-sibling::div//span[@class = 'dx-checkbox-icon']"));
        activeFilterCheckBox.click();

        WebElement okButton = driver.findElement(By.xpath("//span[contains(., 'OK')]"));
        okButton.click();
    }

    public static void goToWorkflows(WebDriver driver, WebDriverWait waiting) {
        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tn-menu/ul/li[11]/a")));
        WebElement siteElementsButton = driver.findElement(By.xpath("//tn-menu/ul/li[11]/a"));
        siteElementsButton.click();

        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Tipos de UT')]")));
        WebElement UTTypesButton = driver.findElement(By.xpath("//span[contains(., 'Workflows')]"));
        UTTypesButton.click();

        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Nuevo WF')]")));
    }

    public static void goToLineasDeTrabajo(WebDriver driver, WebDriverWait waiting)
    {
        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tn-menu/ul/li[11]/a")));
        WebElement siteElementsButton = driver.findElement(By.xpath("//tn-menu/ul/li[11]/a"));
        siteElementsButton.click();

        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Líneas de trabajo')]")));
        WebElement UTTypesButton = driver.findElement(By.xpath("//span[contains(., 'Líneas de trabajo')]"));
        UTTypesButton.click();

        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Nueva Línea de trabajo')]")));
    }

    public static void goToSprints(WebDriver driver, WebDriverWait waiting)
    {
        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tn-menu/ul/li[11]/a")));
        WebElement siteElementsButton = driver.findElement(By.xpath("//tn-menu/ul/li[11]/a"));
        siteElementsButton.click();

        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Sprints')]")));
        WebElement UTTypesButton = driver.findElement(By.xpath("//span[contains(., 'Sprints')]"));
        UTTypesButton.click();

        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Nuevo Sprint')]")));
    }

    public static void borrarBD(WebDriver driver, WebDriverWait waiting)
    {
        driver.get("https://cliente.tuneupprocess.com/ApiWeb/v1/Utils/RestoreBackup_199");

        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'true')]")));
    }

    public static String takeScreenshot(WebDriver driver) throws IOException, InterruptedException {
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File src = takesScreenshot.getScreenshotAs(OutputType.FILE);

        String path = System.getProperty("user.dir") + "/Screenshot/" + System.currentTimeMillis() + ".png";

        File destination = new File(path);

        try
        {
            FileUtils.copyFile(src, destination);
        } catch (Exception e) {}

        return path;
    }

    public static String getCurrentDate()
    {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String strDate = formatter.format(date);

        return strDate;
    }

    public static void logOutWorki(WebDriver driver, WebDriverWait waiting)
    {
        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@src = 'https://cliente.tuneupprocess.com/ApiWeb/v1/Agentes/4/Imagen?idSitio=199']")));
        WebElement imagenPerfil = driver.findElement(By.xpath("//img[@src = 'https://cliente.tuneupprocess.com/ApiWeb/v1/Agentes/4/Imagen?idSitio=199']"));
        imagenPerfil.click();

        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Salir')]")));
        WebElement salirButton = driver.findElement(By.xpath("//span[contains(., 'Salir')]"));
        salirButton.click();
    }
}
