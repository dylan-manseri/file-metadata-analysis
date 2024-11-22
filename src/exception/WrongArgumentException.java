package exception;

import classe.Couleur;

public class WrongArgumentException extends RuntimeException {
    public WrongArgumentException(String message) {

        super(Couleur.ROUGE+"Erreur : "+message + Couleur.RESET);
    }
}
