package classe;
import com.drew.imaging.ImageProcessingException;
import exception.WrongArgumentException;
import java.io.IOException;
import java.nio.file.*;
import java.io.File;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.Directory;
import com.drew.metadata.Tag;

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

    public Fichier(String chemin) throws NoSuchFileException, WrongArgumentException {
        this.chemin= Paths.get(chemin).toAbsolutePath().normalize();
        f = new File(chemin);
        if(!Files.exists(this.chemin)){
            throw new WrongArgumentException("Chemin non existants, taper -h ou --help pour de l'aide");
        }
        if(f.isDirectory()){
            throw new WrongArgumentException(f.getName()+" est un repertoire, taper -d pour les manipuler " +
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

    public String printInfo() throws WrongArgumentException, ImageProcessingException, IOException {
        String nom = chemin.getFileName().toString();
        String type = nom.substring(nom.length()-3);
        if(!(type.equals("jpg") || type.equals("png") || type.equals("webp"))){
            throw new WrongArgumentException("Le fichier entré n'est pas une image, taper -h ou --help pour de l'aide");
        }
        Metadata metadata = ImageMetadataReader.readMetadata(f);
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                // 4. Afficher le nom et la valeur de chaque tag (métadonnée)
                System.out.println(tag.getTagName() + " : " + tag.getDescription());
            }
        }
        String s="a";
        return s;
    }
}
