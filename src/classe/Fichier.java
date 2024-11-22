package classe;
import exception.WrongArgumentException;
import java.io.IOException;
import java.nio.file.*;
import java.io.File;

public class Fichier {
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

    public void printStat(){
        try{
            String nom = chemin.getFileName().toString();
            String type = nom.substring(nom.length()-3);
            String mess="Le fichier "+nom+" est ";

            if(type.equals("txt") || type.equals("csv")){
                mess+="un fichier texte.";
            }
            else if(type.equals("jpg") || type.equals("png") || type.equals("webp")){
                mess+="un fichier image.";
            }

            System.out.println(mess+"\nVoici ses statistiques :");
            String dateCreation = Files.getAttribute(chemin,"creationTime").toString().substring(0,10);
            String dateModification = Files.getLastModifiedTime(chemin).toString().substring(0,10);
            long taille = Files.size(chemin);

            System.out.println("- Date de creation : "+dateCreation);
            System.out.println("- Date de la derniere modification : "+dateModification);
            System.out.println("- Taille : "+taille+" octets");
        } catch(IOException e){
            System.out.println("erreur");
        }
    }
}
