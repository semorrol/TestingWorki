package Utils;

import Utils.DriversConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MyFirefoxDriver {
    private static WebDriver firefoxDriver;
    private static WebDriverWait firefoxWaiting;
    private static MyFirefoxDriver myFirefoxDriver;

    public static MyFirefoxDriver getMyFirefoxDriver(){
        if(myFirefoxDriver == null) {
            myFirefoxDriver = new MyFirefoxDriver();
        }
        return myFirefoxDriver;
    }

    public MyFirefoxDriver(){
        this.firefoxDriver = DriversConfig.iniFirefoxDriver();
        this.firefoxWaiting = new WebDriverWait(this.firefoxDriver, 5);
    }

    public WebDriverWait getFirefoxWaiting() {
        return firefoxWaiting;
    }

    public WebDriver getFirefoxDriver() {
        return firefoxDriver;
    }

    //Cambia el tiempo que espera
    public void setFirefoxWaiting(int wait) {
        this.firefoxWaiting = new WebDriverWait(firefoxDriver, wait);
    }

    public static void closeDriver(){
        firefoxDriver.close();
    }
}
