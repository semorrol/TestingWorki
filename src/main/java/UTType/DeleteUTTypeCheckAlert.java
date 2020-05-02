package UTType;

import Utils.MyFirefoxDriver;
import Utils.TestWithConfig;
import org.ini4j.Wini;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import Utils.Utils;
import java.util.HashMap;
import java.util.List;

public class DeleteUTTypeCheckAlert extends TestWithConfig {

    MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;

    HashMap<String, String> results = new HashMap<>();

    public DeleteUTTypeCheckAlert(Wini commonIni) {
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

        try{
            myFirefoxDriver = MyFirefoxDriver.getMyFirefoxDriver();
            firefoxDriver = myFirefoxDriver.getFirefoxDriver();
            firefoxWaiting = myFirefoxDriver.getFirefoxWaiting();

            results.put("Eliminacion de un tipo sin UT asociadas. Aparece antes un mensaje de confirmacion", deleteUTTypeCheckAlert());

            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return results;
        }

    }

    public String deleteUTTypeCheckAlert(){
        try {
            Utils.goToUTType(firefoxDriver, firefoxWaiting);

            
        } catch (Exception e) {

        }
    }
}
