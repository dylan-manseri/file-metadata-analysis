package core.cli;
import core.cli.save.Compare;
import core.cli.save.Sauvegarde;
import exception.WrongArgumentException;
import java.io.File;
import java.io.IOException;

/**
 * Cette classe centralise l'utilisation des arguments pour simplifié la lecture du code.
 * Elle prend les arguments entres par l'utilisateur et execute la methode associé
 *
 * @author Dylan Manseri
 * @version 0.1
 */

public class Commande {
    String consoleMessage;

    /**
     * Crée une instance de la classe commande et crée les classes en exectuant les methodes associées.
     * Tout cela se fait en fontion des arguments entrée par l'utilisateur
     * @param args les arguments entrée par l'utlisateur
     * @throws Exception si l'utilisateur entre un parametre ou un donnée incorrect, ou que une des fonctionnalité lié
     * au fichier repertoire a échoué.
     */

    public Commande(String[] args) throws Exception {
        switch (args[0]) {
            case "help" -> help();
            case "--help", "-h" -> {
                help();
                System.out.println(consoleMessage);
            }
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
                    System.out.println(fic.getConsoleMessage());
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
                    Recherche r = new Recherche(args[1], args[2]);
                    System.out.println(r.getConsoleMessage());
                }
                else if(args.length==5){
                    Recherche r = new Recherche(args[1], args[2], args[3], args[4]);
                    System.out.println(r.getConsoleMessage());
                }
                else{
                    throw new WrongArgumentException("Commande inconnu taper --help pour de l'aide");
                }
            }
            case "helpGui" -> {
                helpGui();
            }
            default -> {
                throw new WrongArgumentException("Commande inconnu taper --help pour de l'aide");
            }
        }
    }

    /**
     * Cette methode stocke dans un String la liste des commande possible
     */
    private void help(){
        String s1 ="Ci-desosus la liste des commandes disponible.\n";
        s1+="On precise d'abord sur quel support se fera l'analyse avec :\n";
        s1+="------------------------------------------------\n";
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
        consoleMessage=s1;
    }

    /**
     * Cette methode stocke dans un String la liste des commande possible pour le mode console.
     */
    private void helpGui(){
        String s1 ="Ci-desosus la liste des fonctionnalités disponibles.\n";
        s1+="------------------------------------------------\n";
        s1+="Statistiques : Utilise pour les statistique de base d'un fichier/repertoire\n";
        s1+="Metadonnée : Utilise pour les metadonnees d'un fichier\n";
        s1+="Sauvegarder : Utilise pour realiser une sauvegarde d'un repertoire sous forme de capture\n";
        s1+="Compare : Utilise pour comparer un repertoire avec une capture deja enregistrer\n";
        s1+="Recherche : Utilise pour rechercher un fichier selon son nom dans le repertoire courant\n";
        s1+="------------------------------------------------\n";
        s1+="Avec cela s'ajoute la possibilite d'executer toute les fonctionnalités ci-dessus en parcourant manuellement l'arborescence\n";
        consoleMessage=s1;
    }

    /**
     * @return le message de la console à afficher
     */
    public String getConsoleMessage() {
        return consoleMessage;
    }
}
