package core;
import core.save.Compare;
import core.save.Sauvegarde;

import java.io.File;
import java.io.IOException;

/**
 * Cette classe centralise l'utilisation des arguments pour simplifié la lecture du code
 * Elle prend les arguments entrés par l'utilisateur et execute la methode associé
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
        switch (args[0]) {
            case "--help", "-h" -> help();
            case "-d", "--directory" -> {
                Repertoire rep = new Repertoire(args[1]);
                if ((args.length > 2) && (args[2].equals("--stat"))) {
                    rep.printArborescenceDetail();
                } else if ((args.length > 2) && (args[2].equals("-l") || args[2].equals("--list"))) {
                    if (args.length > 3 && args[3].equals("--stat")) {
                        rep.printArborescenceDetail();
                    } else {
                        rep.printArborescence();
                        System.out.println("Pour avoir les statistiques en plus ajouter l'option --stat");
                    }
                }
                if(args.length>2 && args[2].equals("--snapshotsave")){
                    new Sauvegarde(args[1]);
                }
                if(args.length>3 && args[2].equals("--snapshotcompare")){
                    File dos = new File(args[1]);
                    new Compare(dos,args[3]);
                }
            }
            case "-f", "--file" -> {
                Fichier fic = new Fichier(args[1]);
                if ((args.length > 2) && args[2].equals("--stat")) {
                    fic.printStat(true);
                } else if ((args.length > 2) && args[2].equals("--info")) {
                    fic.printInfo();
                }
                if ((args.length > 3) && ((args[2].equals("--info") && args[3].equals("--stat")) ||
                        (args[3].equals("--info") && args[2].equals("--stat")))) {
                    System.out.println("---------Statistiques---------");
                    fic.printStat(true);
                    System.out.println("---------Information----------");
                    fic.printInfo();
                }
            }
            case "--search", "-s" -> {
                if(args.length==3){
                    new Recherche(args[1], args[2]);
                }
                if(args.length==5){
                    new Recherche(args[1], args[2], args[3], args[4]);
                }
            }
        }
    }

    public void help(){
        String s="Ci-desosus la liste des commandes disponible.\n";
        System.out.println(s);
        System.out.println("On précise d'abord sur quel support se fera l'analyse avec :");
        String s1="------------------------------------------------\n";
        s1+= "-d ou --directory: Utilisé pour indiquer un repertoire\n";
        s1+="-f ou --file : Utilisé pour indiquer un fichier\n";
        s1+="------------------------------------------------\n";
        s1+="Ensuite on precise le type d'analyse :\n";
        s1+="------------------------------------------------\n";
        s1+="--stat : Utilisé pour les statistique de base d'un fichier/repertoire\n";
        s1+="--info : Utilisé pour les metadonnées d'un fichier\n";
        s1+="--snapshot : Utilisé pour realiser une sauvegarde d'un fichier et une comparaison avec un autre\n";
        System.out.println(s1);
    }
}
