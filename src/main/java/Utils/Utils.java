package Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Utils {
    public static void loginWorki(WebDriver driver, WebDriverWait waiting, String url, String username, String password){
        driver.get(url);

        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//dx-text-box[@placeholder = 'Email']")));
        WebElement emailInput = driver.findElement(By.xpath("//dx-text-box[@placeholder = 'Email']//input"));
        emailInput.sendKeys(username);

        WebElement passwordInput = driver.findElement(By.xpath("//dx-text-box[@placeholder = 'Password']//input"));
        passwordInput.sendKeys(password);

        WebElement loginButton = driver.findElement(By.xpath("//button[@class = 'btn btn-primary btn-block btn-flat']"));
        loginButton.click();

        //Esperamos hasta que se muestre el logo de la aplicacion para comprobar que se ha logeado correctamente
        waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@class = 'topbar-logo']")));
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
}
