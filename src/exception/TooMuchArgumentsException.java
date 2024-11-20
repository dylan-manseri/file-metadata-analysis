package exception;

public class TooMuchArgumentsException extends Exception{
    public TooMuchArgumentsException(){
        super("\u001B[41m"+"Erreur : Vous ne pouvez pas mettre plus de 2 options." + "\u001B[0m");
    }
}
