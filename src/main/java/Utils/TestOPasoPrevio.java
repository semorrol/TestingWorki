package Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//En esta clase se maneja un Hashmap que se utiliza para saber cuando se ha de actualizar el reporte y cuando no
public class TestOPasoPrevio {
    public static HashMap<String, Boolean> testsPasados = new HashMap<>();

    //Se rellena el hashmap con todos los tests a false inicialmente
    public TestOPasoPrevio(List<String> testsAEjecutar)
    {
        for(String testAEjecutar : testsAEjecutar)
        {
            testsPasados.put(testAEjecutar, false);
        }
    }

    //Metodo para marcar un tests como pasado
    public static void setTestPassed(String testID)
    {
        testsPasados.replace(testID, true);
    }

    //Metodo de consulta para saber si un tests ha sido pasado
    public static boolean getTestPassed(String testID)
    {
        return testsPasados.get(testID);
    }
}
