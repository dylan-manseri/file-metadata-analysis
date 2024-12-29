package core.cli.save;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

/**
 * Cette classe sert a dicter à la librairie GSON ce qu'est un repertoire.
 * Celle-ci peut donc extraire ses attributs et les écrire dans un fichier JSON
 *
 * @author Dylan Manseri
 * @version 1.0
 */
public class DirectoryInfo {
    private String name;
    private ArrayList<FileInfo> fichiersInterne;

    /**
     * Crée un instance de la classe DirectoryInfo et parcourt l'arborescence interne pour les stocker dans fichiersInterne.
     * En parcourant l'arborescence elle extrait en plus les informations des fichiers/dossier et en crée des instances.
     * @param name le nom du dossier
     * @param fichiersInterne tableau des fichiers/dossiers contenu dans le dossier
     * @throws IOException si la lecture des donnée a échoué
     */
    public DirectoryInfo(String name, File[] fichiersInterne) throws IOException {
        this.name=name;
        ArrayList<DirectoryInfo> dossierInterne= new ArrayList<>();
        String nom, taille;
        ArrayList<FileInfo> fichiers = new ArrayList<>();
        for (File file : fichiersInterne) {
            if(file.isDirectory()){
                DirectoryInfo dir = new DirectoryInfo(file.getName(),file.listFiles());
                dossierInterne.add(dir);
            }
            else{
                nom = file.getName();
                taille = String.valueOf(Files.size(file.toPath()));
                BasicFileAttributes attr =Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                String creationTime = String.valueOf(attr.creationTime());
                FileInfo f = new FileInfo(nom, taille, creationTime);
                fichiers.add(f);
            }

        }
        this.fichiersInterne=fichiers;
    }

    /**
     * getter retournant le nom du dossier
     * @return l'attribut name
     */
    public String getName(){
        return this.name;
    }

}
