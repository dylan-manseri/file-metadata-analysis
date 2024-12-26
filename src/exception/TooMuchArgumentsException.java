package exception;
import core.Couleur;

/**
 * Cette exception permet de gérer le surplus d'argument entré par l'utilisateur.
 *
 * @author Dylan Manseri
 * @version 0.1
 */

public class TooMuchArgumentsException extends Exception{
    public TooMuchArgumentsException(){
        super(Couleur.ROUGE +"Erreur : Vous ne pouvez pas mettre plus de 2 options." + Couleur.RESET);
    }
}
