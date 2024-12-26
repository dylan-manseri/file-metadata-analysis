package core;
import exception.WrongArgumentException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;

/**
 * Cette classe gère les repertoires entrés par l'utilisateur ou ceux rencontré lors de parcours de tableaux.
 * Elle permet d'obtenir une arborescence simplifiée/detaillée de l'organisation d'un repertoire.
 * Elle permet d'obtenir les statistiques d'un repertoire et de ses sous fichiers.
 *
 * @author Dylan Manseri
 * @version 0.1
 */

public class Repertoire implements EstAnalysable {
    private final Path chemin;
    private final File rep;

    public Repertoire(Path chemin) throws WrongArgumentException {
        if(!Files.exists(chemin)){
            throw new WrongArgumentException("Chemin non existants, taper -h ou --help pour de l'aide");
        }
        this.chemin=chemin;
        rep = new File(chemin.toString());
        if(rep.isFile()){
            throw new WrongArgumentException(rep.getName()+" est un fichier, taper -f pour les manipuler " +
                    "taper -h ou --help pour de l'aide");
        }
    }

    public Repertoire(File rep) throws NoSuchFileException, WrongArgumentException {
        this.rep=rep;
        this.chemin=rep.toPath();
        if(!Files.exists(chemin)){
            throw new NoSuchFileException("Chemin non existants, taper -h ou --help pour de l'aide");
        }
        if(rep.isFile()){
            throw new WrongArgumentException(rep.getName()+" est un fichier, taper -f pour les manipuler\n " +
                    "taper -h ou --help pour de l'aide");
        }
    }

    public Repertoire(String chemin) throws WrongArgumentException {
        this.chemin= Paths.get(chemin).toAbsolutePath().normalize();
        rep = new File(chemin);
        if(!Files.exists(this.chemin)){
            throw new WrongArgumentException("Chemin non existants, taper -h ou --help pour de l'aide");
        }
        if(rep.isFile()){
            throw new WrongArgumentException(rep.getName()+" est un fichier, taper -f pour les manipuler\n " +
                    "taper -h ou --help pour de l'aide");
        }
    }

    public void printArborescence(File[] fichiers, int i, boolean estDansUnDossier, String s){
        if(i==0 && !estDansUnDossier){
            System.out.println("\nArborescence simplifié de "+s+"\n");
            System.out.println(s);
            s="";
        }
        if(i==fichiers.length){
            return;
        }
        else if(i==fichiers.length-1){
            File fichier=fichiers[i];
            System.out.println(s+"└──"+fichier.getName());
            if(fichier.isDirectory()){
                File[] fichierInterne = fichier.listFiles();
                printArborescence(fichierInterne,0,true,s+"│   ");
            }
        }
        else{
            File fichier=fichiers[i];
            System.out.println(s+"├──"+fichier.getName());
            if(fichier.isDirectory()){
                File[] fichierInterne = fichier.listFiles();
                printArborescence(fichierInterne,0,true,s+"│   ");
            }
        }
        printArborescence(fichiers,i+1,false,s);
    }

    public void printArborescence() {
        File[] fichiers = rep.listFiles();
        printArborescence(fichiers,0,false,rep.getName());
    }

    public void printArborescenceDetail(File[] fichiers, int i, boolean estDansUnDossier, String s) throws IOException, WrongArgumentException {
        if(i==0 && !estDansUnDossier){
            System.out.println("\nArborescence detaillee de "+s+"\n");
            System.out.println(s);
            s="";
        }
        if(i==fichiers.length){
            return;
        }
        else if(i==fichiers.length-1){
            File fichier=fichiers[i];
            if(fichier.isFile()){
                Fichier f = new Fichier(fichier);
                System.out.println(s+"└──"+fichier.getName()+f.printStat(false));
            }
            else{
                Repertoire rep = new Repertoire(fichier);
                System.out.println(s+"└──"+fichier.getName()+rep.printStat(false));
                File[] fichierInterne = fichier.listFiles();
                printArborescenceDetail(fichierInterne,0,true,s+"│   ");
            }
        }
        else{
            File fichier=fichiers[i];
            if(fichier.isFile()){
                Fichier f = new Fichier(fichier);
                System.out.println(s+"├──"+fichier.getName()+f.printStat(false));
            }
            if(fichier.isDirectory()){
                Repertoire rep = new Repertoire(fichier);
                System.out.println(s+"├──"+fichier.getName()+rep.printStat(false));
                File[] fichierInterne = fichier.listFiles();
                printArborescenceDetail(fichierInterne,0,true,s+"│   ");
            }
            System.out.println(s+"│");
        }
        printArborescenceDetail(fichiers,i+1,false,s);
    }

    public void printArborescenceDetail() throws IOException, WrongArgumentException {
        File[] fichiers = rep.listFiles();
        printArborescenceDetail(fichiers,0,false,rep.getName());
    }

    public String printStat(boolean ecrire) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(chemin, BasicFileAttributes.class);
        String s=" est un repertoire ";
        s+="| "+"Date de creation : "+attr.creationTime().toString().substring(0,10);
        s+=" | "+"Date de modification : "+attr.lastModifiedTime().toString().substring(0,10);
        s+=" | "+"Taille : "+attr.size()+" octets |";
        if(ecrire){
            System.out.println(s);
        }
        return s;
    }

    public void printInfo(){
        String s="a";
    }
}
