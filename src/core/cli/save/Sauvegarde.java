package core.cli.save;

import com.google.gson.Gson;
import exception.WrongArgumentException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class Sauvegarde {
    private String directoryPath;

    public Sauvegarde(String directoryPath) throws WrongArgumentException, IOException {
        this.directoryPath=directoryPath;
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
}
