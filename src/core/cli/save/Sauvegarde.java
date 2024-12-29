package core.cli.save;

import com.google.gson.Gson;
import exception.WrongArgumentException;
import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * Cette classe permet la sauvegarde d'un dossier, elle écrit les information d'un fichier
 * dans un fichier JSON et stocke ce fichier dans un repertoire crée au préalable
 *
 * @author Dylan Manseri
 * @version 1.0
 */
public class Sauvegarde {
    private final HashMap<String, String> donnee=new HashMap<>();

    /**
     * Crée une instance de la classe Sauvegarde et fait directement la sauvegarde
     * @param directoryPath chemin du dossier a sauvegarder entré par l'utilisateur
     * @throws WrongArgumentException si le dossier est invalide ou inexistant
     * @throws IOException si la lecture des informations du dossier a échoué
     */
    public Sauvegarde(String directoryPath) throws WrongArgumentException, IOException {
        File directory = new File(directoryPath);
        if(directory.isFile()){
            throw new WrongArgumentException("Ce fichier n'est pas un dossier, la sauvegarde ne concerne que les dossiers");
        }
        String name = directory.getName();
        File[] fichiersInterne = directory.listFiles();
        DirectoryInfo dir = new DirectoryInfo(name,fichiersInterne);
        Gson gson = new Gson();
        String json = gson.toJson(dir);
        File save = new File("save");
        Path s = save.toPath();
        createDirectory("save");
        String chemin = createFile(s.resolve("save").toString());
        try (FileWriter writer = new FileWriter(chemin)) {
            writer.write(json);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crée une instance de la classe Sauvegarde et fait directement la sauvegarde
     * Ce deuxième constructeur sert au mode graphique
     * @param cheminTxt bloc de texte où l'utilisateur a écrit le chemin
     * @throws WrongArgumentException si le dossier est invalide ou inexistant
     * @throws IOException si l'extraction des informations du dossier a échoué
     */
    public Sauvegarde(JTextField cheminTxt) throws WrongArgumentException, IOException {
        String directoryPath= cheminTxt.getText();
        File directory = new File(directoryPath);
        if(!directory.isDirectory()){
            throw new WrongArgumentException("Ce fichier n'est pas un dossier, la sauvegarde ne concerne que les dossiers");
        }
        String name = directory.getName();
        File[] fichiersInterne = directory.listFiles();
        DirectoryInfo dir = new DirectoryInfo(name,fichiersInterne);
        Gson gson = new Gson();
        String json = gson.toJson(dir);
        File save = new File("save");
        Path s = save.toPath();
        createDirectory("save");
        createFileGui(s.resolve("save").toString());
        String chemin = donnee.get("saveName");
        try (FileWriter writer = new FileWriter(chemin)) {
            writer.write(json);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crée un repertoire dans le repertoire courant appelé "save"
     * @param chemin chemin où le repertoire doit être crée
     * @throws WrongArgumentException si la création du dossier a échoué
     */
    public void createDirectory(String chemin) throws WrongArgumentException {
        File rep = new File(chemin);
        if(!rep.exists()){
            if(rep.mkdir()){
                System.out.println("Dossier save cree avec succes dans votre repertoire courant");
            }
            else{
                throw new WrongArgumentException("Erreur lors de la creation du fichier save");
            }
        }
    }

    /**
     * Crée le fichier save dans le repertoire crée dédié tout en respectant l'odre de nom des précedant fichiers save crée
     * @param chemin chemin où la capture doit être crée
     * @return chemin où la capture a été crée
     * @throws IOException si la création du fichier a échoué
     * @throws WrongArgumentException si le nom du fichier n'est pas le bon
     */
    public String createFile(String chemin) throws IOException, WrongArgumentException {
        int i=0;
        boolean creation=false;
        chemin+=String.valueOf(i);
        File fic;
        while(!creation){
            chemin=chemin.substring(0,chemin.length()-1);
            chemin+=String.valueOf(i);
            fic = new File(chemin);
            if(!fic.exists()) {
                if(fic.createNewFile()) {
                    System.out.println("Sauvegarde du fichier cree avec succee dans le dossier dedie");
                    creation=true;
                } else {
                    throw new WrongArgumentException("Erreur lors de la creation du fichier save");
                }
            }
            i++;
        }
        return chemin;
    }

    /**
     * Crée le fichier save dans le repertoire crée dédié tout en respectant l'odre de nom des précedant fichiers save crée.
     * Cette methode sert au constructeur utilisé pour le mode graphique
     * @param chemin chemin où la capture doit être crée
     * @throws IOException si la création du fichier a échoué
     * @throws WrongArgumentException si le nom du fichier n'est pas le bon
     */
    public void createFileGui(String chemin) throws IOException, WrongArgumentException {
        int i=0;
        boolean creation=false;
        chemin+=String.valueOf(i);
        File fic;
        String txt = "";
        while(!creation){
            chemin=chemin.substring(0,chemin.length()-1);
            chemin+=String.valueOf(i);
            fic = new File(chemin);
            if(!fic.exists()) {
                if(fic.createNewFile()) {
                    txt ="Sauvegarde du fichier cree avec succee dans le dossier dedie";
                    creation=true;
                } else {
                    throw new WrongArgumentException("Erreur lors de la creation du fichier save");
                }
            }
            i++;
        }
        donnee.put("saveName",chemin);
        donnee.put("message",txt);
    }

    /**
     * Cette methode renvoie le message liée à la reussite de l'operation stocké pendant création du fichier
     * Cette methode utile qu'au methode utile au mode graphique
     * @return le message stocké dans la HashMap des données
     */
    public String getMessage(){
        return donnee.get("message");
    }
}
