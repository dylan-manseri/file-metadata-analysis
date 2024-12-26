package core;

import java.io.IOException;

/**
 * Cette interface a pour objectif d'ograniser les classes pour simplifier leur comprehension
 *
 * @author  Dylan Manseri
 * @version 0.1
 */

public interface EstAnalysable {
    public String printStat(boolean ecrire) throws IOException;
    public void printInfo() throws Exception;
}
