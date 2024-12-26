package main;
import core.cli.Commande;
import exception.TooMuchArgumentsException;
import exception.WrongArgumentException;
import java.io.IOException;


public class MainCli {
    public static void main(String[] args){
        try{
            if(args.length==0 || args.length>5){
                throw new WrongArgumentException("Commande inconnu taper --help pour de l'aide");
            }
            new Commande(args);
        } catch(TooMuchArgumentsException | WrongArgumentException | IOException e){
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}