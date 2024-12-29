package exception;

import core.cli.Couleur;

/**
 * Cette exception permet de gérer les erreurs d'arguments de l'utilisateur.
 *
 * @author Dylan Manseri
 * @version 0.1
 */

public class WrongArgumentException extends Exception{

    /**
     * Instancie WrongArgumentException et affiche un message en rouge
     * @param message le message à écrire
     */
    public WrongArgumentException(String message) {

        super(Couleur.ROUGE+"Erreur : "+ message + Couleur.RESET);
    }
}
