package Workflow;

import Utils.TestWithConfig;
import Utils.Utils;
import org.ini4j.Wini;
import Utils.MyFirefoxDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.List;

public class NewWorkflow extends TestWithConfig {

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    private HashMap<String, String> results = new HashMap<>();

    public NewWorkflow(Wini commonIni) {
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

            results.put("Creates a workflow", createWorkflow());

            return results;
        } catch (Exception e)
        {
            e.printStackTrace();
            return results;
        }
    }

    private String createWorkflow()
    {
        try
        {
            Utils.goToWorkflows(firefoxDriver, firefoxWaiting);

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Nuevo WF')]")));
            WebElement nuevoWF = firefoxDriver.findElement(By.xpath("//span[contains(., 'Nuevo WF')]"));
            nuevoWF.click();

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[span[contains(., 'Nuevo workflow')]]/following-sibling::div//input[@class = 'dx-texteditor-input']")));
            WebElement workflowNameInput = firefoxDriver.findElement(By.xpath("//div[span[contains(., 'Nuevo workflow')]]/following-sibling::div//input[@class = 'dx-texteditor-input']"));
            workflowNameInput.sendKeys("Workflow selenium");

            WebElement crearWF = firefoxDriver.findElement(By.xpath("//span[contains(., 'Crear')]"));
            crearWF.click();

            try
            {
                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(., 'Workflow selenium')]")));
            } catch (Exception e)
            {
                e.printStackTrace();
                return e.toString() + "\nERROR. No se ha podido el workflow";
            }

            return "Test OK. Se ha creado un workflow";
        } catch (Exception e)
        {
            e.printStackTrace();
            return e.toString() + "\nERROR. No se ha podido crear el workflow";
        }
    }

}
