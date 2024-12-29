package core.cli.save;

/**
 * Cette classe sert a dicter à la librairie GSON ce qu'est un fichier.
 * Celle-ci peut donc extraire ses attributs et les écrire dans un fichier JSON
 *
 * @author Dylan Manseri
 * @version 1.0
 */
public class FileInfo {
    private String name;
    private String taille;
    private String creationTime;

    /**
     * Crée une instance de la classe FileInfo via DirectoryInfo
     * @param name nom du fichier
     * @param taille taille du fichier
     * @param creationTime date de creation du fichier
     */
    public FileInfo(String name, String taille, String creationTime){
        this.name=name;
        this.taille=taille;
        this.creationTime=creationTime;
    }

    public String getName(){
        return this.name;
    }

    public String getTaille(){
        return this.taille;
    }

    public String getCreationTime(){
        return this.creationTime;
    }
}
