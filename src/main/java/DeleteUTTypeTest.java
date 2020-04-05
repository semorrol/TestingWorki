import Utils.DriversConfig;
import Utils.Test;
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

public class DeleteUTTypeTest extends TestWithConfig {
    static String url;
    static String headless;
    static String username;
    static String password;

    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    HashMap<String, String> results = new HashMap<>();

    public DeleteUTTypeTest(Wini commonIni) {
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

        try{
            firefoxDriver = DriversConfig.headlessOrNot(headless);
            firefoxWaiting = new WebDriverWait(firefoxDriver, 5);

            results.put("Delete all UT types  ->  ", deleteUTTypes());

            return results;
        } catch(Exception e){
            e.printStackTrace();
            return results;
        } finally {
            firefoxDriver.close();
        }
    }

    public String deleteUTTypes()
    {
        try{
            Utils.loginWorki(firefoxDriver, firefoxWaiting, url, username, password);
        } catch (Exception e){
            e.printStackTrace();
            return e.toString() + "\nERROR. The login on Worki failed";
        }

        try{
            Utils.goToUTType(firefoxDriver, firefoxWaiting);

            Utils.showNonActiveUTTypes(firefoxDriver, firefoxWaiting);

            firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@class = 'dx-datagrid-table dx-datagrid-table-fixed']//tbody[@role = 'presentation']" +
                    "/tr")));

            for(int i = 0; i < 3  ; i++) {
                WebElement thunderButton = firefoxDriver.findElement(By.xpath("//table[@class = 'dx-datagrid-table dx-datagrid-table-fixed']//tbody[@role = 'presentation']/tr[1]" +
                        "//button[@class = 'tn-button--dropdown']"));
                thunderButton.click();

                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Eliminar')]")));
                WebElement deleteButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Eliminar')]"));
                deleteButton.click();

                firefoxWaiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(., 'Sí')]")));
                WebElement yesButton = firefoxDriver.findElement(By.xpath("//span[contains(., 'Sí')]"));
                yesButton.click();
            }

            return "Test OK. All the UT types were deleted";
        } catch (Exception e){
            e.printStackTrace();
            return e.toString() + "\nERROR. Failed while trying to delete the UT types";
        }
    }
}
