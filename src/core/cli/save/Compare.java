package core.cli.save;

import com.google.gson.Gson;
import exception.WrongArgumentException;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *Cette classe permet la comparaison d'un dossier donnée avec une capture (sauvegarde d'un autre dossier).
 *Elle crée un fichier JSON des informations d'un dossier et le compare avec la capture donnée.
 *
 * @author Dylan Manseri
 * @version 1.0
 */
public class Compare {
    private File doss;
    private final String captureName;
    private String reponseConsole;


    /**
     * Crée un instance de la classe Compare et fais directement la comparaison selon les paramètres.
     * @param doss object de la classe File correspondant au fichier a comparer.
     * @param captureName nom de la capture.
     * @throws WrongArgumentException si l'un des deux paramètre est incorrect.
     * @throws IOException si la comparaison a échoué.
     */
    public Compare(File doss, String captureName) throws WrongArgumentException, IOException {
        this.doss = doss;
        this.captureName = captureName;
        if (!doss.isDirectory()) {
            throw new WrongArgumentException("Ce chemin n'est pas un repertoire, taper --help pour de l'aide");
        }
        String json = createJson(doss);
        String capture = readJsonCapture(captureName);
        if(json.equals(capture)){
            System.out.println("Le fichier n'a pas subit de modifications");
        }
        else{
            System.out.println("Le fichier a subit des modifications");
        }
    }

    /**
     * Crée un instance de la classe Compare et fais directement la comparaison selon les paramètres.
     * Ce constructeur est dédié au mode graphique uniquement.
     * @param doss object de la classe File correspondant au fichier a comparer.
     * @param captureName nom de la capture écrite dans le bloc de texte.
     * @throws WrongArgumentException si l'un des deux paramètre est incorrect.
     * @throws IOException si la comparaison a échoué.
     */
    public Compare(File doss, JTextField captureName) throws WrongArgumentException, IOException {
        this.doss = doss;
        this.captureName = captureName.getText();
        if (!doss.isDirectory()) {
            throw new WrongArgumentException("Ce chemin n'est pas un repertoire, taper --help pour de l'aide");
        }
        String json = createJson(doss);
        String capture = readJsonCapture(this.captureName);
        if(json.equals(capture)){
            this.reponseConsole = "Le fichier n'a pas subit de modifications";
        }
        else{
            this.reponseConsole = "Le fichier a subit des modifications";
        }
    }

    /**
     * Crée un fichier JSON repertoriant les informations d'un dossier
     * @param dos l'objet issus de la classe File dont les informations vont être extraite
     * @return le fichier JSON
     * @throws IOException si l'extraction des informations a echoué
     */
    public String createJson(File dos) throws IOException {
        String name = dos.getName();
        File[] fichiersInterne = dos.listFiles();
        DirectoryInfo rep = new DirectoryInfo(name, fichiersInterne);
        Gson gson = new Gson();
        return gson.toJson(rep);
    }

    /**
     * Cette methode cherche dans le repertoire courant la capture
     * @param captureName le nom de la capture entrée par l'utilisateur
     * @return le contenu de la capture
     * @throws WrongArgumentException si le nom de la capture est incorrect
     * @throws IOException si la lecture a echoué
     */
    public String readJsonCapture(String captureName) throws WrongArgumentException, IOException {
        File saveFolder = new File("save");
        File save;
        if (saveFolder.exists() && saveFolder.isDirectory()) {
            Path savePath = saveFolder.toPath().resolve(captureName);
            save = savePath.toFile();
            if (save.exists()) {
                return Files.readString(savePath);
            } else {
                throw new WrongArgumentException("nom de la capture invalide");
            }
        } else {
            throw new WrongArgumentException("dossier save introuvable");
        }
    }

    /**
     * getter retournant la reponse de la console, dedié au mode graphique uniquement
     * @return l'attribut reponseConsole
     */
    public String getReponseConsole(){
        return this.reponseConsole;
    }

    /**
     * getter retournant le fichier qui a été comparé
     * @return l'attribut doss
     */
    public File getDoss() {
        return doss;
    }

    /**
     * setter modifiant l'attribut doss (dossier comparé)
     */
    public void setDoss(File doss) {
        this.doss = doss;
    }
}
