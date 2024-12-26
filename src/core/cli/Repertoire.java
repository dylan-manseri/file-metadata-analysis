package core.cli;
import exception.WrongArgumentException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.util.HashMap;

/**
 * Cette classe gère les repertoires entrés par l'utilisateur ou ceux rencontré lors de parcours de tableaux.
 * Elle permet d'obtenir une arborescence simplifiée/detaillée de l'organisation d'un repertoire.
 * Elle permet d'obtenir les statistiques d'un repertoire et de ses sous fichiers.
 *
 * @author Dylan Manseri
 * @version 0.1
 */

public class Repertoire{
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
            System.out.println("\nArborescence simplifie de "+s+"\n");
            System.out.println(s);
            s="";
        }
        if(i==fichiers.length){
            return;
        }
        else if(i==fichiers.length-1){
            File fichier=fichiers[i];
            System.out.println(s+" [_"+fichier.getName());
            if(fichier.isDirectory()){
                File[] fichierInterne = fichier.listFiles();
                printArborescence(fichierInterne,0,true,s+"|   ");
            }
        }
        else{
            File fichier=fichiers[i];
            System.out.println(s+"|--"+fichier.getName());
            if(fichier.isDirectory()){
                File[] fichierInterne = fichier.listFiles();
                printArborescence(fichierInterne,0,true,s+"|   ");
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
            System.out.println(s+printStat(false));
            s="";
        }
        if(i==fichiers.length){
            return;
        }
        else if(i==fichiers.length-1){
            File fichier=fichiers[i];
            if(fichier.isFile()){
                Fichier f = new Fichier(fichier);
                System.out.println(s+" [_"+fichier.getName()+f.printStat(false));
            }
            else{
                Repertoire rep = new Repertoire(fichier);
                System.out.println(s+" [_"+fichier.getName()+rep.printStat(false));
                File[] fichierInterne = fichier.listFiles();
                printArborescenceDetail(fichierInterne,0,true,s+"|   ");
            }
        }
        else{
            File fichier=fichiers[i];
            if(fichier.isFile()){
                Fichier f = new Fichier(fichier);
                System.out.println(s+"|--"+fichier.getName()+f.printStat(false));
            }
            if(fichier.isDirectory()){
                Repertoire rep = new Repertoire(fichier);
                System.out.println(s+"|--"+fichier.getName()+rep.printStat(false));
                File[] fichierInterne = fichier.listFiles();
                printArborescenceDetail(fichierInterne,0,true,s+"|   ");
            }
            System.out.println(s+"|");
        }
        printArborescenceDetail(fichiers,i+1,false,s);
    }

    public void printArborescenceDetail() throws IOException, WrongArgumentException {
        File[] fichiers = rep.listFiles();
        printArborescenceDetail(fichiers,0,false,rep.getName());
    }

    public String printStat(boolean ecrire) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(chemin, BasicFileAttributes.class);
        File[] fichiers = rep.listFiles();
        String s;
        HashMap<String, Integer> h = new HashMap<>();
        h.put(".png",0);
        h.put(".jpg",0);
        h.put(".webp",0);
        int png;
        int jpg;
        int webp;
        if(fichiers!=null){
            h=nbrFichier(fichiers,0,h);
            png=h.get(".png"); jpg=h.get(".jpg"); webp=h.get(".webp");
            s=" est un repertoire contenant "+png+" fichiers.png, "+jpg+" fichiers.jpg, "+webp+" fichiers.webp ";
        }
        else{
            s=" est un repertoire vide";
        }
        s+="| "+"Date de creation : "+attr.creationTime().toString().substring(0,10);
        s+=" | "+"Date de modification : "+attr.lastModifiedTime().toString().substring(0,10);
        s+=" | "+"Taille : "+attr.size()+" octets |";
        if(ecrire){
            System.out.println(s);
        }
        return s;
    }

    public HashMap<String, Integer> nbrFichier(File[] fichiers, int i, HashMap<String, Integer> nbr){
        if(i==fichiers.length){
            return nbr;
        }
        File ieFile = fichiers[i];
        if(ieFile.isDirectory()){
            File[] fichiersInterne = ieFile.listFiles();
            if(fichiersInterne!=null){
                nbr=nbrFichier(fichiersInterne,0,nbr);
            }
            return nbrFichier(fichiers,i+1,nbr);
        }
        nbr=verifFile(nbr,ieFile);
        return nbrFichier(fichiers,i+1,nbr);
    }

    public HashMap<String, Integer> verifFile(HashMap<String, Integer> h, File f){
        String name = f.getName();
        if(name.endsWith(".png")){
            int v = (int) h.get(".png");
            v++;
            h.put(".png",v);
        }
        if(name.endsWith(".jpg")){
            int v = (int) h.get(".jpg");
            v++;
            h.put(".jpg",v);
        }
        if(name.endsWith(".webp")){
            int v = (int) h.get(".webp");
            v++;
            h.put(".webp",v);
        }
        return h;
    }

    public String getName(){
        return chemin.getFileName().toString();
    }
}
