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
    /**
     * chemin du repertoire
     */
    private final Path chemin;
    /**
     * objet de la classe File correspondant au repertoire
     */
    private final File rep;

    /**
     * Crée une instance de Repertoire et vérifie le dossier associé existe bien.
     * @param rep instance de File, c'est le fichier liée au repertoire
     * @throws NoSuchFileException si repertoire n'existe pas
     * @throws WrongArgumentException si le fichiers entrée est un fichier et non un repertoire
     */
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

    /**
     * Crée une instance de Repertoire et vérifie le dossier associé existe bien.
     * Ce 2e constrcteur sert aux cas où nous n'avons que le chemin.
     * @param chemin chemin du repertoire
     * @throws WrongArgumentException si le repertoire est un fichier ou s'il n'existe pas
     */
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

    /**
     * Parcours recursivement l'arborescence et l'affiche de manière simplifié
     * @param fichiers tableau des fichiers/dossiers contenus dans le repertoire
     * @param i itération de la récursivité
     * @param estDansUnDossier booléen utile à l'affiche indiquant si l'on se trouve au tout debut du programme ou non
     * @param s String à écrire
     */
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

    /**
     * Surchage de la methode initial qui sert à préparer les variables néccessaires au bon fonctionnement de la methode initial
     * Cette methode est celle qui sera appelé pour utilisé la methode inital, car plus simple.
     */
    public void printArborescence() {
        File[] fichiers = rep.listFiles();
        printArborescence(fichiers,0,false,rep.getName());
    }

    /**
     * Parcours recursivement l'arborescence et l'affiche avec les statistiques basiques
     * @param fichiers tableau des fichiers/dossiers contenus dans le repertoire
     * @param i itération de la récursivité
     * @param estDansUnDossier booléen utile à l'affiche indiquant si l'on se trouve au tout debut du programme ou non
     * @param s String à écrire
     * @throws IOException si l'extraction des statistiques a échoué
     * @throws WrongArgumentException si l'extraction des statistiques a échoué
     */
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

    /**
     * Surchage de la methode initial qui sert à préparer les variables néccessaires au bon fonctionnement de la methode initial
     * Cette methode est celle qui sera appelé pour utilisé la methode inital, car plus simple.
     */
    public void printArborescenceDetail() throws IOException, WrongArgumentException {
        File[] fichiers = rep.listFiles();
        printArborescenceDetail(fichiers,0,false,rep.getName());
    }

    /**
     * Stocke les statistiques d'un dossier dans une variable et l'écrit ou la renvoie en fonction des parameètre
     * @param ecrire booléen indiquant si il faut écrire les statistiques ou non
     * @return une String stockant les statistiques
     * @throws IOException si l'extraction des statistiques a échouée
     */
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
    /**
     * Stocke les statistiques d'un dossier dans une variable et l'écrit ou la renvoie en fonction des paramètre.
     * Cette methode ne sert qu'au mode graphique
     * @return une String stockant les statistiques
     * @throws IOException si l'extraction des statistiques a échouée
     */
    public String printStatGui() throws IOException {
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
            s=rep.getName()+" est un repertoire contenant \n"+png+" fichiers.png, \n"+jpg+" fichiers.jpg,\n"+webp+" fichiers.webp \n";
        }
        else{
            s=" est un repertoire vide\n";
        }
        s+="Date de creation : "+attr.creationTime().toString().substring(0,10)+"\n";
        s+="Date de modification : "+attr.lastModifiedTime().toString().substring(0,10)+"\n";
        s+="Taille : "+attr.size()+" octets"+"\n";
        return s;
    }

    /**
     * Parcours recursivement l'arborescence et compte le nombre de fichiers images selon leur extention (jpg, png...)
     * @param fichiers fichiers/dossier à parcourir
     * @param i itération de la récursivité
     * @param nbr HashMap du nombre de fichiers images selon le format
     * @return la HashMap(format, nombre) resultant du parcours
     */
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

    /**
     * Verifie par le nom le format d'un fichier et modifie la HashMap en conséquence
     * @param h la HashMap du nombre de fichiers images selon le format
     * @param f instance File du fichier
     * @return la HashMap(format, nombre) resultant de la verification
     */
    public HashMap<String, Integer> verifFile(HashMap<String, Integer> h, File f){
        String name = f.getName();
        if(name.endsWith(".png")){
            int v = h.get(".png");
            v++;
            h.put(".png",v);
        }
        if(name.endsWith(".jpg")){
            int v = h.get(".jpg");
            v++;
            h.put(".jpg",v);
        }
        if(name.endsWith(".webp")){
            int v = h.get(".webp");
            v++;
            h.put(".webp",v);
        }
        return h;
    }

    /**
     * getter retournant le nom du repertoire
     * @return le nom du repertoire
     */
    public String getName(){
        return chemin.getFileName().toString();
    }
}
