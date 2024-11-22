package exception;
import classe.Couleur;

public class TooMuchArgumentsException extends Exception{
    public TooMuchArgumentsException(){
        super(Couleur.ROUGE +"Erreur : Vous ne pouvez pas mettre plus de 2 options." + Couleur.RESET);
    }
}
