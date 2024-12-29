package main;
import core.cli.Commande;
import exception.TooMuchArgumentsException;
import exception.WrongArgumentException;
import java.io.IOException;

/**
 * Classe principale gérant plusieurs fonctionnalité en fonction des arguments entrée par l'utilisateur
 * Fonctionnalité :
 * - Extraction des statistiques d'un fichier/repertoire
 * - Extraction des metadonnées d'une images
 * - Recherche d'un fichier selon plusieurs critères dans une arborescence
 * - Realisation d'une sauvegarde d'un repertoire
 * - Comparaison d'une sauvegarde avec un autre repertoire
 * @author Dylan Manseri
 * @version 1.0
 */
public class MainCli {
    /**
     * Execute le programme et instance Commande selon les arguments entrée par l'utilisateurs
     * @param args arguments entrée par l'utilisateurs
     */
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