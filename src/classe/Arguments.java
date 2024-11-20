package classe;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.NoSuchFileException;

public class Arguments {
    private String[] arguments;

    public Arguments(String[] args) throws NoSuchFileException {
        this.arguments=args;
        if(this.arguments[0].equals("--help") || this.arguments[0].equals("-h")){
            help();
        }
        else if(this.arguments[0].equals("-d") || this.arguments[0].equals("--directory")){
            Path chemin = Paths.get(args[1]);
            if(!Files.exists(chemin)){
                throw new NoSuchFileException("Chemin non existants, taper -h ou --help pour de l'aide");
            }
            System.out.println(chemin.toAbsolutePath());
        }
    }

    public void help(){
        String s="t'es paumax?";
        System.out.println(s);
    }

}
