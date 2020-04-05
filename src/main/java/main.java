import Utils.Test;
import exceptions.MissingParameterException;
import org.ini4j.Profile;
import org.ini4j.Wini;
import Utils.Color;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {
    public static void main(String[] args) {
        if (args.length == 0)
        {
            System.out.println(Color.RED + "ERROR. You need to pass at least one argument with the path of the inicialization file" + Color.RESET);
            System.exit(0);
        }

        try
        {
            Wini commonIni = getConfig(args);
            Map<String, Test> tests = initializeTests(commonIni);
            List<String> testsList = getTestsToRun(commonIni);
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
        tests.put("newUTType", new NewUTTypeTest(config));
        tests.put("editUTType", new EditUTTypeTest(config));
        tests.put("deleteUTType", new DeleteUTTypeTest(config));

        return tests;
    }

    private static Wini getConfig(String[] args) throws Exception
    {
        Wini commonIni = new Wini(new File(args[0]));

        int otherParamsStart = 1;

        //Override or add parameters to commonIni when a second .ini file is passed as argument
        /*if(args.length > 1 && args[1].contains(".ini"))
        {
            otherParamsStart = 2;
            Wini selfIni = new Wini(new File(args[1]));
            //Get the sections and parameters of the second .ini
            for(String sectionName : selfIni.keySet())
            {
                Profile.Section section = selfIni.get(sectionName);
                for(String parameterName : section.keySet())
                {
                    commonIni.put(sectionName, parameterName, selfIni.get(sectionName, parameterName));
                }
            }
        }

        // Sobreescribir cuando se nos pasan parámetros con el formato Section.Parameter=LoQueSea
        for (int i = otherParamsStart; i < args.length; i++)
        {
            Pattern p = Pattern.compile("(.+)\\.(.+)=(.+)");
            Matcher m = p.matcher(args[i]);
            if (m.matches())
            {
                System.out.println(m.group(1) + m.group(2) + m.group(3));
                commonIni.put(m.group(1), m.group(2), m.group(3));
            }
        }*/

        return commonIni;
    }

    public static List<String> getTestsToRun(Wini config)
    {
        String testsListAsString = config.get("General", "testsList");
        List<String> testsToRun = testsListAsString != null ? Arrays.asList(testsListAsString.split(",")) : new ArrayList<>();
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
    }
}