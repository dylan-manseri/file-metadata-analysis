package exception;

import core.Couleur;

/**
 * Cette exception permet de gérer les erreurs d'arguments de l'utilisateur.
 *
 * @author Dylan Manseri
 * @version 0.1
 */

public class WrongArgumentException extends Exception{
    public WrongArgumentException(String message) {

        super(Couleur.ROUGE+"Erreur : "+ message + Couleur.RESET);
    }
}
