import LineaDeTrabajo.NewLineaConColaborador;
import LineaDeTrabajo.NewLineaDeTrabajo;
import Login.LoginWorki;
import Sprint.DeleteSprint;
import Sprint.NewSprintValoresObligatorios;
import UTType.*;
import UTs.CheckValoresObligatorios;
import UTs.NewUTYAbrir;
import UTs.NewUTyNueva;
import Utils.*;
import Workflow.NewWorkflow;
import Workflow.NewWorkflowConLinea;
import exceptions.MissingParameterException;
import org.ini4j.Profile;
import org.ini4j.Wini;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {

    static MyFirefoxDriver myFirefoxDriver;
    static WebDriver firefoxDriver;
    static WebDriverWait firefoxWaiting;
    static TestOPasoPrevio testOPasoPrevio;

    public static void main(String[] args) {
        if (args.length == 0)
        {
            System.out.println(Color.RED + "ERROR. You need to pass at least one argument with the path of the inicialization file" + Color.RESET);
            System.exit(0);
        }

        //Restaura la base de datos
        myFirefoxDriver = MyFirefoxDriver.getMyFirefoxDriver();
        firefoxDriver = myFirefoxDriver.getFirefoxDriver();
        firefoxWaiting = myFirefoxDriver.getFirefoxWaiting();

        try
        {
            new Report(); //inicializa la clase Report para manejar los reportes
            Wini commonIni = getConfig(args);
            Map<String, Test> tests = initializeTests(commonIni);
            List<String> testsList = getTestsToRun(commonIni);
            testOPasoPrevio = new TestOPasoPrevio(new ArrayList<>(tests.keySet())); //Se crea el Hashmap para manejar cuando un test se debe añadir al reporte y cuando no
            runTests(testsList, tests);
        }
        catch (MissingParameterException e2) {
            System.out.println("\u001B[31m" + e2.getMessage() + "\u001B[0m\n");
        }
        catch(Exception e)
        {
            System.out.println("\u001B[31m" + "ERROR. The path of the inicialization file is incorrect" + "\u001B[0m\n" + e.toString());
        }
    }

    private static Map<String, Test> initializeTests(Wini config)
    {
        Map<String, Test> tests = new HashMap<>();

        tests.put("loginWorki", new LoginWorki(config));
        tests.put("newUTTypeByDefault", new NewUTTypeByDefault(config));
        tests.put("newUTTypeConLinea", new NewUTTypeConLinea(config));
        tests.put("newUTTypeNonActive", new NewUTTypeNonActive(config));
        tests.put("newUTTypeWithFailure", new NewUTTypeWithFailure(config));
        tests.put("checkUTTypeNameAlreadyExists", new CheckUTTYpeNameAlreadyExists(config));
        tests.put("editUTType", new EditUTType(config));
        tests.put("deleteUTType", new DeleteUTTypeCheckAlert(config));
        tests.put("editUTTypeAddingImage", new EditUTTypeAddingImage(config));
        tests.put("newLineaTrabajo", new NewLineaDeTrabajo(config));
        tests.put("newLineaConColaborador", new NewLineaConColaborador(config));
        tests.put("newWorkflow", new NewWorkflow(config));
        tests.put("newWorkflowConLinea", new NewWorkflowConLinea(config));
        tests.put("newUTYAbrir", new NewUTYAbrir(config));
        tests.put("newUTYNueva", new NewUTyNueva(config));
        tests.put("checkValoresObligatorios", new CheckValoresObligatorios(config));
        tests.put("newSprintValoresObligatorios", new NewSprintValoresObligatorios(config));
        tests.put("deleteSprint", new DeleteSprint(config));


        return tests;
    }

    private static Wini getConfig(String[] args) throws Exception
    {
        Wini commonIni = new Wini(new File(args[0]));

        //Override or add parameters to commonIni when a second .ini file is passed as argument
        if(args.length > 1)
        {
            commonIni.put("General", "testsList", args[1]);
        }

        return commonIni;
    }

    public static List<String> getTestsToRun(Wini config)
    {
        String testsListAsString = config.get("General", "testsList");
        List<String> testsToRun = testsListAsString != null ? Arrays.asList(testsListAsString.split(",")) : new ArrayList<String>();
        return testsToRun;
    }

    public static void runTests(List<String> testsList, Map<String, Test> tests) throws Exception
    {
        //Prints the results
        for (String testName : testsList)
        {
            if(tests.containsKey(testName))
            {
                System.out.println(Color.CYAN + "Beggining test: " + testName + Color.RESET);

                //Borra el contenido de la base de datos antes de lanzar cada test
                Utils.borrarBD(firefoxDriver,firefoxWaiting);

                HashMap<String, String> results = tests.get(testName).check();
                for (String name: results.keySet()){
                    String key = name.toString();
                    String value = results.get(name).toString();
                    if(value.contains("OK")) System.out.println(Color.CYAN + key + Color.RESET + " " + Color.GREEN + value + Color.RESET);
                    else if(value.contains("ERROR")) System.out.println(Color.CYAN + key + Color.RESET + " " + Color.RED + value + Color.RESET);
                    else if(value.contains("WARNING")) System.out.println(Color.CYAN + key + Color.RESET + " " + Color.PURPLE + value + Color.RESET);

                }

            }
            else
            {
                System.err.println(Color.RED + "The argument: '" + testName + "' from the attribute 'testsList' on the .ini file do not match with the test suite {calls, coordinator, productivityAgents, productivityExport, oldRecordDownload}" + Color.RESET);
            }
        }
        MyFirefoxDriver.closeDriver();
    }
}