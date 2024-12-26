package core.save;

import com.google.gson.Gson;
import exception.WrongArgumentException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Compare {
    File dos;
    String captureName;

    public Compare(File dos, String captureName) throws WrongArgumentException, IOException {
        this.dos = dos;
        this.captureName = captureName;
        if (!dos.isDirectory()) {
            throw new WrongArgumentException("Ce chemin n'est pas un repertoire, taper --help pour de l'aide");
        }
        String json = createJson(dos);
        String capture = readJsonCapture(captureName);
        if(json.equals(capture)){
            System.out.println("Le fichier n'a pas subit de modifications");
        }
        else{
            System.out.println("Le fichier a subit des modifications");
        }
    }

    public String createJson(File dos) throws IOException {
        String name = dos.getName();
        File[] fichiersInterne = dos.listFiles();
        DirectoryInfo rep = new DirectoryInfo(name, fichiersInterne);
        Gson gson = new Gson();
        String json = gson.toJson(rep);
        return json;
    }

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
        /*if(saveFolder.exists() && saveFolder.isDirectory()){
            File[] saveList = saveFolder.listFiles();
            if(saveList!=null){
                int i=0;
                int taille=saveList.length;
                while(i<taille && !saveList[i].getName().equals(captureName)){
                    i++;
                }
                if(i==taille){
                    throw new WrongArgumentException("nom de la capture invalide");
                }
                else{

                }
            }
            for(File file : saveList){
                if(file.getName().equals(captureName)){
                    save=file;
                }
            }
        }*/
    }

}
