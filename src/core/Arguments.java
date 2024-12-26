package core;
import core.save.Compare;
import core.save.Sauvegarde;
import exception.WrongArgumentException;

import java.io.File;
import java.io.IOException;

/**
 * Cette classe centralise l'utilisation des arguments pour simplifié la lecture du code
 * Elle prend les arguments entres par l'utilisateur et execute la methode associé
 *
 * @author Dylan Manseri
 * @version 0.1
 */

public class Arguments {

    /**
     *
     * @param args
     * @throws IOException
     */

    public Arguments(String[] args) throws Exception {
        if(args.length==0){
            throw new WrongArgumentException("Commande inconnu taper --help pour de l'aide");
        }
        switch (args[0]) {
            case "--help", "-h" -> help();
            case "-d", "--directory" -> {
                if(args.length==1){throw new WrongArgumentException("Commande inconnu taper --help pour de l'aide");}
                Repertoire rep = new Repertoire(args[1]);
                if ((args.length == 3) && (args[2].equals("--stat"))) {
                    System.out.println(rep.getName()+rep.printStat(false));
                } else if ((args.length > 2) && (args[2].equals("-l") || args[2].equals("--list"))) {
                    if (args.length == 4 && args[3].equals("--stat")) {
                        rep.printArborescenceDetail();
                    }
                    else if(args.length==3){
                        rep.printArborescence();
                        System.out.println("Pour avoir les statistiques en plus ajouter l'option --stat");
                    }
                    else{
                        throw new WrongArgumentException("Commande inconnu taper --help pour de l'aide");
                    }
                }
                else if(args.length==3 && args[2].equals("--snapshotsave")){
                    new Sauvegarde(args[1]);
                }
                else if(args.length==4 && args[2].equals("--snapshotcompare")){
                    File dos = new File(args[1]);
                    new Compare(dos,args[3]);
                }
                else{
                    throw new WrongArgumentException("Commande inconnu taper --help pour de l'aide");
                }
            }
            case "-f", "--file" -> {
                if(args.length==1){throw new WrongArgumentException("Commande inconnu taper --help pour de l'aide");}
                Fichier fic = new Fichier(args[1]);
                if ((args.length == 3) && args[2].equals("--stat")) {
                    fic.printStat(true);
                }
                else if ((args.length == 3) && args[2].equals("--info")) {
                    fic.printInfo();
                }
                else if ((args.length == 4) && ((args[2].equals("--info") && args[3].equals("--stat")) ||
                        (args[3].equals("--info") && args[2].equals("--stat")))) {
                    System.out.println("---------Statistiques---------");
                    fic.printStat(true);
                    System.out.println("---------Information----------");
                    fic.printInfo();
                }
                else{
                    throw new WrongArgumentException("Commande inconnu taper --help pour de l'aide");
                }
            }
            case "--search", "-s" -> {
                if(args.length==3){
                    new Recherche(args[1], args[2]);
                }
                else if(args.length==5){
                    new Recherche(args[1], args[2], args[3], args[4]);
                }
                else{
                    throw new WrongArgumentException("Commande inconnu taper --help pour de l'aide");
                }
            }
            default -> {
                throw new WrongArgumentException("Commande inconnu taper --help pour de l'aide");
            }
        }
    }

    public void help(){
        String s="Ci-desosus la liste des commandes disponible.\n";
        System.out.println(s);
        System.out.println("On precise d'abord sur quel support se fera l'analyse avec :");
        String s1="------------------------------------------------\n";
        s1+= "-d ou --directory: Utilise pour indiquer un repertoire\n";
        s1+="-f ou --file : Utilise pour indiquer un fichier\n";
        s1+="------------------------------------------------\n";
        s1+="Une fois le type de fichier indique suivi de son chemin on precise le type d'analyse :\n";
        s1+="------------------------------------------------\n";
        s1+="REPERTOIRE/FICHIER --stat : Utilise pour les statistique de base d'un fichier/repertoire\n";
        s1+="FICHIER --info : Utilise pour les metadonnees d'un fichier\n";
        s1+="REPERTOIRE --snapshotsave : Utilise pour realiser une sauvegarde d'un repertoire sous forme de capture\n";
        s1+="REPERTOIRE --snapshotcompare CAPTURENAME : Utilise pour comparer un repertoire avec une capture deja enregistrer\n";
        s1+="------------------------------------------------\n";
        s1+="Commande ne necessitant pas de chemin en option\n";
        s1+="------------------------------------------------\n";
        s1+="--search --name NAME : Utilise pour rechercher un fichier selon son nom dans le repertoire courant\n";
        s1+="--search --year YEAR : Utilise pour rechercher un fichier selon son annee de creation dans le repertoire courant\n";
        s1+="--search --date YEAR-MONTH-DAY : Utilise pour rechercher un fichier selon sa date de creation dans le repertoire courant\n";
        s1+="--search --dim LARGEURxHAUTEUR : Utilise pour rechercher un fichier selon sa dimension dans le repertoire courant\n";
        System.out.println(s1);
    }
}
