package core.cli;
import exception.WrongArgumentException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class Recherche {
    private final String option1;
    private String donnee1;
    private File fileFound;
    private String consoleMessage;

    public Recherche(String o, String d) throws IOException, WrongArgumentException {
        this.option1 =o;
        this.donnee1 =d;
        File dossier = new File (".");
        File[] fichiers = dossier.listFiles();
        if(fichiers==null){
            throw new WrongArgumentException("Ce fichier est vide, recherche impossible");
        }
        switch (option1) {
            case "--name"-> {
                Path res = rechercheParNom(donnee1,fichiers,0,fichiers[0].toPath()).toAbsolutePath().normalize();
                String pathName = res.getFileName().toString();
                boolean trouve = true;
                int mini = Math.min(donnee1.length(),pathName.length());
                for(int i=0; i<mini; i++){
                    if (pathName.charAt(i) != donnee1.charAt(i)) {
                        trouve = false;
                        break;
                    }
                }
                if(trouve){
                    consoleMessage = "1 fichiers image trouve : "+ res;
                }
                else{
                    consoleMessage = "Aucun fichier trouve";
                }
            }
            case "--year" -> {
                ArrayList<Path> result = new ArrayList<>();
                result= rechercheParAnnee(donnee1,fichiers,0,result);
                Iterator<Path> it = result.iterator();
                consoleMessage = result.size()+" fichiers trouvees images en "+ donnee1 +" :\n";
                while(it.hasNext()){
                    consoleMessage += it.next().getFileName()+"\n";
                }
            }
            case "--date" -> {
                ArrayList<Path> result = new ArrayList<>();
                result= rechercheParDate(donnee1,fichiers,0,result);
                int taille = result.size();
                if(taille>0){
                    consoleMessage = result.size()+" fichiers images trouvees le "+ donnee1 +" :\n";
                    for (Path path : result) {
                        consoleMessage += path.getFileName()+"\n";
                    }
                }
                else{
                    consoleMessage = "Aucune fichier image trouve le "+ donnee1;
                    consoleMessage += "\nAttention le format de date attendu est YEAR-MONTH-DAY";
                }

            }
            case "--dim" -> {
                ArrayList<Path> result = new ArrayList<>();
                if(donnee1.contains("x")){
                    String x = donnee1.substring(0, donnee1.indexOf("x"));
                    String y = donnee1.substring(donnee1.indexOf("x")+1);
                    int largeur = Integer.parseInt(x);
                    int hauteur = Integer.parseInt(y);
                    result = rechercheParDim(largeur,hauteur,fichiers,0,result);
                    int taille = result.size();
                    if(taille>0){
                        consoleMessage = taille+" fichiers image trouve de dimension >= "+ donnee1 +" :\n";
                        for (Path path : result) {
                            consoleMessage += path.getFileName() + "\n";
                        }
                    }
                    else{
                        consoleMessage = taille+" fichiers images trouve de taille >= "+ donnee1;
                    }
                }
                else{
                    throw new WrongArgumentException("Donnee incorrect, merci la dimension comme ci-dessous\nLARGEURxHAUTEUR");
                }

            }
            case "gui" ->{
                File parent = new File("..");
                fileFound = recherchePartielParNom(donnee1, fichiers, 0, parent);
            }
        }
    }

    public Recherche(String option1, String option2, String donnee1, String donnee2) throws WrongArgumentException, IOException {
        this.option1=option1;
        this.donnee1=donnee1;
        File dossier = new File (".");
        File[] fichiers = dossier.listFiles();
        if(fichiers==null){
            throw new WrongArgumentException("Ce fichier est vide, recherche impossible");
        }
        if (option1.equals("--year") && option2.equals("--dim") || option1.equals("--dim") && option2.equals("--year")) {
            if(option1.equals("--dim")){
                String s = donnee2;
                donnee2 =donnee1;
                this.donnee1=s;
                donnee1=this.donnee1;

            }
            ArrayList<Path> result1 = new ArrayList<>();
            result1 = rechercheParAnnee(donnee1, fichiers, 0, result1);
            ArrayList<Path> result2 = new ArrayList<>();
            int largeur;
            int hauteur;
            try{
                String x = donnee2.substring(0, donnee2.indexOf("x"));
                String y = donnee2.substring(donnee2.indexOf("x") + 1);
                largeur = Integer.parseInt(x);
                hauteur = Integer.parseInt(y);
                result2 = rechercheParDim(largeur, hauteur, fichiers, 0, result2);
            } catch (StringIndexOutOfBoundsException | NumberFormatException | IOException e) {
                throw new WrongArgumentException("Donnee incorrect, merci la dimension comme ci-dessous\nLARGEURxHAUTEUR");
            }
            result1.addAll(result2);
            result1 = rechercheParDimAnnee(largeur, hauteur, donnee1, result1);
            int taille = result1.size();
            if(taille>0){
                consoleMessage = taille+" fichiers trouve de taille >= "+ donnee2 +" datant de "+donnee1+" :\n";
                for (Path path : result1) {
                    consoleMessage += path.getFileName()+"\n";
                }
            }
            else{
                consoleMessage = """
                        Aucun fichier trouver\s
                        Syntaxe : ANNEE LARGEURxHAUTEUR,\
                        Help pour de l'aide""";
            }
        }
    }

    private Path rechercheParNom(String name, File[] fichiers, int i, Path result){
        if(i==fichiers.length){
            return result;
        }
        File f = fichiers[i];
        Path fPath;
        if(f.isDirectory()){
            File[] fichiersInterne = f.listFiles();
            if(fichiersInterne != null && fichiersInterne.length!=0) {
                fPath = rechercheParNom(name, fichiersInterne, 0, fichiersInterne[0].toPath());
            }
            else{
                return rechercheParNom(name, fichiers, i+1,result);
            }
        }
        else{
            fPath = f.toPath();
        }
        if(isImage(fPath)){
            if(fPath.getFileName().toString().equals(name)){
                result =  fPath;
                return result;
            }
            if(compareStr(name, fPath.getFileName().toString()) > compareStr(name,result.getFileName().toString())){
                result=fPath;
            }
        }
        return rechercheParNom(name, fichiers, i+1,result);
    }

    private ArrayList<Path> rechercheParAnnee(String annee, File[] fichiers, int i, ArrayList<Path> result) throws IOException {
        if(i==fichiers.length){
            return result;
        }
        File f = fichiers[i];
        if(f.isDirectory()){
            File[] fichiersInterne = f.listFiles();
            if(fichiersInterne != null && fichiersInterne.length!=0) {
                result = rechercheParAnnee(annee, fichiersInterne, 0, result);
                return rechercheParAnnee(annee,fichiers,i+1,result);
            }
            else{
                return rechercheParAnnee(annee,fichiers,i+1,result);
            }
        }
        if(isImage(f.toPath())){
            String dateCreation = getCreationDate(f);
            String anneeFichier = dateCreation.substring(0,dateCreation.indexOf("-"));
            if(annee.equals(anneeFichier)){
                result.add(f.toPath());
            }
        }
        return rechercheParAnnee(annee,fichiers,i+1,result);
    }

    private ArrayList<Path> rechercheParDate(String date, File[] fichiers, int i, ArrayList<Path> result) throws IOException {
        if(i==fichiers.length){
            return result;
        }
        File f = fichiers[i];
        if(f.isDirectory()){
            File[] fichiersInterne = f.listFiles();
            if(fichiersInterne != null && fichiersInterne.length!=0) {
                result = rechercheParDate(date, fichiersInterne, 0, result);
                return rechercheParDate(date,fichiers,i+1,result);
            }
            else{
                return rechercheParDate(date,fichiers,i+1,result);
            }
        }
        if(isImage(f.toPath())){
            String dateCreation = getCreationDate(f);
            if(date.equals(dateCreation)){
                result.add(f.toPath());
            }
        }
        return rechercheParDate(date,fichiers,i+1,result);
    }

    private ArrayList<Path> rechercheParDim(int x, int y, File[]fichiers, int i, ArrayList<Path> result) throws IOException {
        if(i==fichiers.length){
            return result;
        }
        File f = fichiers[i];
        if(f.isDirectory()){
            File[] fichiersInterne = f.listFiles();
            if(fichiersInterne != null && fichiersInterne.length!=0) {
                result = rechercheParDim(x,y, fichiersInterne, 0, result);
                return rechercheParDim(x,y,fichiers,i+1,result);
            }
            else{
                return rechercheParDim(x,y,fichiers,i+1,result);
            }
        }
        if(isImage(f.toPath())){
            BufferedImage image = ImageIO.read(f);
            if(image!=null){
                int[] dim = getDimension(image);
                if(dim[0]>=x && dim[1]>=y){
                    result.add(f.toPath());
                }
            }

        }
        return rechercheParDim(x,y,fichiers,i+1,result);
    }

    private ArrayList<Path> rechercheParDimAnnee(int x, int y, String annee,ArrayList<Path> fichiers) throws IOException {
        Iterator<Path> it = fichiers.iterator();
        ArrayList<Path> result = new ArrayList<>();
        int i; BufferedImage im;
        while(it.hasNext()){
            Path p = it.next();
            File f = p.toFile();
            im=ImageIO.read(f);
            if(verifAnnee(annee,f) && im!=null && verifDim(x,y,im)){
                if(!result.contains(p)){
                    i= fichiers.indexOf(p);
                    result.add(fichiers.get(i));
                }
            }
        }
        return result;
    }

    private File recherchePartielParNom(String name, File[] fichiers, int i, File parent){
        if(i==fichiers.length){
            return parent;
        }
        File f = fichiers[i];
        if(f.getName().equals(name)){
            return f;
        }
        if(f.isDirectory()){
            File[] fichiersInterne = f.listFiles();
            File fichierInterneTrouve;
            if(fichiersInterne != null && fichiersInterne.length!=0) {
                fichierInterneTrouve = recherchePartielParNom(name, fichiersInterne, 0, parent);
                if(fichierInterneTrouve.getName().equals(name)){
                    return fichierInterneTrouve;
                }
            }
            else{
                return recherchePartielParNom(name, fichiers, i+1, parent);
            }
        }
        return recherchePartielParNom(name, fichiers, i+1, parent);
    }

    private boolean verifDim(int x, int y, BufferedImage im) throws IOException {
        int[] dim = getDimension(im);
        return dim[0] >= x && dim[1] >= y;
    }

    private boolean verifAnnee(String annee, File f) throws IOException {
        String dateCreation = getCreationDate(f);
        String anneeFichier = dateCreation.substring(0,dateCreation.indexOf("-"));
        return anneeFichier.equals(annee);
    }

    private int[] getDimension(BufferedImage image) throws IOException {
        int largeur = image.getWidth();
        int hauteur = image.getHeight();
        return new int[]{largeur,hauteur};
    }

    private int compareStr(String var1, String var2){
        if(var1==null || var2==null){
            return 0;
        }
        int longDonnee = var1.length();
        int longIter =  var2.length();
        int k=0;
        while((k<longDonnee && k<longIter) && (var1.charAt(k)==var2.charAt(k))){
            k++;
        }
        return k;
    }

    private boolean isImage(Path p){
        String nom = p.getFileName().toString();
        if(p.toFile().isFile()){
            int index = nom.lastIndexOf(".");
            if (index > 0 && index < nom.length() - 1) {
                String type = nom.substring(index);
                return type.equals(".jpg") || type.equals(".png") || type.equals(".jpeg") || type.equals(".webp");
            }
        }
        return false;
    }

    public boolean isImage(){
        String nom = fileFound.getName();
        if(fileFound.isFile()){
            int index = nom.lastIndexOf(".");
            if (index > 0 && index < nom.length() - 1) {
                String type = nom.substring(index);
                return type.equals(".jpg") || type.equals(".png") || type.equals(".jpeg") || type.equals(".webp");
            }
        }
        return false;
    }

    public String getCreationDate(File f) throws IOException {
        return Files.getAttribute(f.toPath(),"creationTime").toString().substring(0,10);
    }

    public String getConsoleMessage() {
        return consoleMessage;
    }

    public File getFileFound(){
        return fileFound;
    }

}
