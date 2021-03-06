package Login;

import Utils.TestWithConfig;
import Utils.Utils;
import Utils.TestOPasoPrevio;
import Utils.Report;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.ini4j.Wini;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import Utils.MyFirefoxDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LoginWorki extends TestWithConfig {

    static final String TEST_ID = "loginWorki";
    static final String TEST_NAME = "Login Worki";

    static String url;
    static String headless;
    static String username;
    static String password;

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    HashMap<String, String> results = new HashMap<>();


    public LoginWorki(Wini commonIni) {
        super(commonIni);
    }

    @Override
    public HashMap<String, List<String>> getRequiredParameters() {
        HashMap<String, List<String>> requiredParameters = new HashMap<>();

        requiredParameters.put("General", new ArrayList<>(Arrays.asList("url", "headless")));
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
            myFirefoxDriver = MyFirefoxDriver.getMyFirefoxDriver();
            firefoxDriver = myFirefoxDriver.getFirefoxDriver();
            firefoxWaiting = myFirefoxDriver.getFirefoxWaiting();

            results.put("-- Login on worki  ->  ", LoginWorki());

            return results;
        } catch (Exception e){
            e.printStackTrace();
            return results;
        }
    }

    public String LoginWorki() throws IOException, InterruptedException {
        try{
            Utils.loginWorki(firefoxDriver, firefoxWaiting, url, username, password);

            Report.testPassed(TEST_ID, TEST_NAME, "Se ha iniciado sesión correctamente");

            return "Test OK. The Login on Worki works";
        } catch (Exception e) {
            Report.testFailed(TEST_NAME, "No se ha podido iniciar sesión", ExceptionUtils.getStackTrace(e), firefoxDriver);

            e.printStackTrace();
            return e.toString() + "\nERROR. Login on worki failed";
        }
    }
}
