package classe;
import com.drew.imaging.ImageProcessingException;
import exception.WrongArgumentException;

import java.io.IOException;

/**
 * Cette interface a pour objectif d'ograniser les classes pour simplifier leur comprehension
 *
 * @author  Dylan Manseri
 * @version 0.1
 */

public interface EstAnalysable {
    public String printStat() throws IOException;
    public String printInfo() throws WrongArgumentException, ImageProcessingException, IOException;
}
