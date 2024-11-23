package classe;
import exception.WrongArgumentException;
import java.io.IOException;
import java.nio.file.*;
import java.io.File;

/**
 * Cette classe gere l'utilisation des fichiers entré par l'utilisateur
 * Elle permet d'obtenir ses statistiques et ses informations plus détaillés
 *
 * @author Dylan Manseri
 * @version 0.1
 */

public class Fichier implements EstAnalysable {
    private final Path chemin;
    private final File f;

    public Fichier(Path chemin) throws NoSuchFileException, WrongArgumentException {
        if(!Files.exists(chemin)){
            throw new NoSuchFileException("Chemin non existants, taper -h ou --help pour de l'aide");
        }
        this.chemin=chemin;
        f = new File(chemin.toString());
        if(f.isDirectory()){
            throw new WrongArgumentException(f.getName()+" est un repertoire, taper -d pour les manipuler \n" +
                    "taper -h ou --help pour de l'aide");
        }
    }

    public Fichier(File f) throws NoSuchFileException, WrongArgumentException {
        this.f=f;
        this.chemin=f.toPath();
        if(!Files.exists(chemin)){
            throw new NoSuchFileException("Chemin non existants, taper -h ou --help pour de l'aide");
        }
        if(f.isDirectory()){
            throw new WrongArgumentException(f.getName()+" est un repertoire, taper -d pour les manipuler \n" +
                    "taper -h ou --help pour de l'aide");
        }
    }

    public String printStat(){
        try{
            String mere = chemin.getParent().getFileName().toString();
            String nom = chemin.getFileName().toString();
            String type = nom.substring(nom.length()-3);
            String mess=" est ";

            if(type.equals("txt") || type.equals("csv")){
                mess+="un fichier texte";
            }
            else if(type.equals("jpg") || type.equals("png") || type.equals("webp")){
                mess+="un fichier image";
            }
            String dateCreation = Files.getAttribute(chemin,"creationTime").toString().substring(0,10);
            String dateModification = Files.getLastModifiedTime(chemin).toString().substring(0,10);
            long taille = Files.size(chemin);

            String s="| Date de creation : "+dateCreation+" ";
            s+="| Date de la derniere modification : "+dateModification;
            s+="| Taille : "+taille+" octets |";
            return mess+" dans "+mere+"->"+s;
        } catch(IOException e){
            System.out.println("erreur");
        }
        return null;
    }

    public String printInfo(){
        String s="a";
        return s;
    }
}
