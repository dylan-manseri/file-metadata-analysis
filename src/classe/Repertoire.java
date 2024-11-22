package classe;
import exception.WrongArgumentException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Repertoire {
    private final Path chemin;
    private final File rep;

    public Repertoire(Path chemin) throws NoSuchFileException {
        if(!Files.exists(chemin)){
            throw new NoSuchFileException("Chemin non existants, taper -h ou --help pour de l'aide");
        }
        this.chemin=chemin;
        rep = new File(chemin.toString());
        if(rep.isFile()){
            throw new WrongArgumentException(rep.getName()+" est un fichier, taper -f pour les manipuler\n " +
                    "taper -h ou --help pour de l'aide");
        }
    }

    public void listFile(boolean estDansUnDossier) throws WrongArgumentException, NoSuchFileException {
        File[] fichiers = rep.listFiles();
        String ROUGE = Couleur.ROUGE;
        String RESET = Couleur.RESET;

        if(fichiers!=null){
            System.out.println("\nle repertoire "+chemin.getFileName()+" a "+ROUGE+fichiers.length+RESET+" fichier/s :");
            int i=0;

            for (File fichier : fichiers) {
                Path p = Paths.get(fichier.getAbsolutePath());
                p=p.normalize();
                i++;

                if(estDansUnDossier){
                    System.out.println("\n"+fichier.getName()+":");
                }
                else{
                    System.out.println("--------------------------------------------------");
                    System.out.println("\n"+ROUGE+i+". "+fichier.getName()+RESET+":\n");
                }

                if(fichier.isFile()){
                    Fichier f = new Fichier(p);
                    f.printStat();
                }
                if(fichier.isDirectory()){
                    Repertoire rep = new Repertoire(p);
                    rep.listFile(true);
                }
            }
        }
    }
}
