package core.cli.save;

public class FileInfo {
    private String name;
    private String taille;
    private String creationTime;


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
