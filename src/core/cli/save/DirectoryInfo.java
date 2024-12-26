package core.cli.save;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class DirectoryInfo {
    private String name;
    private ArrayList<FileInfo> fichiersInterne;
    private ArrayList<DirectoryInfo> dossierInterne;

    public DirectoryInfo(String name, File[] fichiersInterne) throws IOException {
        this.name=name;
        ArrayList<DirectoryInfo> dossierInterne= new ArrayList<>();
        this.dossierInterne=dossierInterne;
        String nom, taille;
        ArrayList<FileInfo> fichiers = new ArrayList<>();
        for (File file : fichiersInterne) {
            if(file.isDirectory()){
                DirectoryInfo dir = new DirectoryInfo(file.getName(),file.listFiles());
                this.dossierInterne.add(dir);
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

    public String getName(){
        return this.name;
    }

    public ArrayList<FileInfo> getFichiersInterne(){
        return this.fichiersInterne;
    }
}
