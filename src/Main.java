import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

//classe constructeur methode

public class Main {
    public static void main(String[] args) throws TooMuchArgumentsException {
        try{
            if(args.length>2){
                throw new TooMuchArgumentsException();
            }
            Arguments a = new Arguments(args);
        }catch(TooMuchArgumentsException e){
            System.out.println(e.getMessage());
        }
        /*if(args[0].equals("-h") || args[0].equals("--help")){
            Argument a=new Argument("-h");
        }
        FileSystem fs = FileSystems.getDefault();
        Path chemin = fs.getPath("C:\\Users\\dylnm\\Desktop\\projet japo\\retour.png\\");
        System.out.println(chemin.getNameCount()+"\n");
        try{
            BasicFileAttributes attr = Files.readAttributes(chemin,BasicFileAttributes.class);
            System.out.println("date de creation "+attr.creationTime());
            System.out.println("\ndate de la derniere modif "+attr.lastModifiedTime());
            System.out.println("\ntaille "+attr.size());
        } catch(IOException e){
            System.out.println("erreur");
        }*/

    }
}