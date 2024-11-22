package classe;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.NoSuchFileException;

public class Arguments {
    private final String[] arguments;

    public Arguments(String[] args) throws NoSuchFileException {
        this.arguments=args;
        if(this.arguments[0].equals("--help") || this.arguments[0].equals("-h")){
            help();
        }
        else if(this.arguments[0].equals("-d") || this.arguments[0].equals("--directory")){
            Path p = Paths.get(this.arguments[1]).toAbsolutePath().normalize();
            String nom = p.getFileName().toString();
            Repertoire rep=new Repertoire(p);
            if((this.arguments.length > 2) && (this.arguments[2].equals("-l") || this.arguments[2].equals("--list"))){
                rep.listFile(false);
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
