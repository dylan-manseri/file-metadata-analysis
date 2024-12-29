package core.cli;
import exception.WrongArgumentException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Cette sert à la recherche d'un fichier dans une arboresence de repertoire
 * Il peut rechercher selon des critère qui seront entrée par l'utilisateur
 *
 * @author Dylan Manseri
 * @version 1.0
 */
public class Recherche {
    /**
    critère de la recherche
     */
    private final String option1;
    /**
     * donnee entrée par l'utilisateur
     */
    private String donnee1;
    /**
     * les fichier trouvé si il est trouvé
     */
    private File fileFound;
    /**
     * resultat de la recherche
     */
    private String consoleMessage;

    /**
     * Instancie la classe recherche et fais la recherche selon le crière
     * @param o type de critere de la recherche
     * @param d critere de la rechercher
     * @throws IOException si l'analyse d'un fichier lors du parcours de l'arborescence provoque une erreur
     * @throws WrongArgumentException si l'argument entrée est incorrect
     */
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

    /**
     * Instancie la classe recherche et fais la recherche selon deux critère (année et dimension).
     * Ce constructeur sert au cas de figure où l'utilisateur décide de faire une recherche
     * selon deux critère et prend en compte le cas où il inverse les deux ecritures.
     * @param option1 type de critere (--year ou --dim) entrée par l'utilisateur
     * @param option2 2e type de critere (--year ou --dim) entrée par l'utilisateur
     * @param donnee1 critère de la recherche (l'année ou la dimension)
     * @param donnee2 2e critère de la recherche (l'année ou la dimension)
     * @throws WrongArgumentException si le repertoire courant est invalide
     * @throws IOException si l'analyse d'un fichier lors du parcourt de l'arborescence provoque une erreur
     */
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
                Iterator<Path> it = result1.iterator();
                while(it.hasNext()) {
                    consoleMessage += it.next().getFileName()+"\n";
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

    /**
     * Algorithme recursif parcourant l'arborescence et compare toutes les images entre elles selon leur nom.
     * @param name nom du fichier complet ou incomplet dont la recherche est l'objet
     * @param fichiers ensembles des fichiers/dossiers du repertoire courant
     * @param i iteration de la recursivité
     * @param result resultat courant, est modifiable à chaque iteration si la ressemblance est meilleure
     * @return le chemin du fichiers trouvé
     */
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

    /**
     * Algorithme recursif parcourant l'arborescence et compare leur année de création avec celle du critère.
     * @param annee année du fichier complete dont la recherche est l'objet
     * @param fichiers ensembles des fichiers/dossiers du repertoire courant
     * @param i iteration de la recursivité
     * @param result tableau repertoriant tous les fichiers crée la même année que l'année entrée
     * @return le tableau de tous les fichiers respectant le critère
     * @throws IOException si l'extraction de l'année d'un fichier à échoué
     */
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

    /**
     * Algorithme recursif parcourant l'arborescence et compare leur date avec celle du critère.
     * @param date date de création complète du fichier dont la recherche est l'objet.
     * @param fichiers ensembles des fichiers/dossiers du repertoire courant.
     * @param i itération de la récursivité.
     * @param result tableau repartoriant tous les fichiers crée à la date entrée.
     * @return le tableau de tous les fichiers respectant le critère.
     * @throws IOException si l'extraction de l'année d'un fichiers à échoué.
     */
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

    /**
     * Algorithme recursif parcourant l'arborescence et compare leur dimension avec celle du critère.
     * @param x largeur dont la recherche est l'objet
     * @param y longueur dont la recherche est l'objet
     * @param fichiers ensemble des fichiers/dossier du repertoire courant
     * @param i itération de la recursivité
     * @param result tableau répertoriant tous les fichiers images crée pendant l'année entrée
     * @return le tableau de tous les fichiers images respectant le critère.
     * @throws IOException si l'extraction de la dimension à échooué
     */
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

    /**
     * Methode centralisant le recherche pour deux critère et met le resultat dans un tableau.
     * Cette methode appelle les deux methodes associé aux critère de l'année et de la dimension.
     * Elle crée un tableau où ets stocké toutes les valeurs et des deux tableaux en supprimant les doublons.
     * @param x largeur dont la recherche est l'objet.
     * @param y longueur dont la recherche est l'objet.
     * @param annee année du fichier complete dont la recherche est l'objet.
     * @param fichiers ensemble des fichiers/dossiers du repertoire courant.
     * @return le tableau de tous les fichiers images respectant les critère.
     * @throws IOException si l'extraction de la dimension à échooué .
     */
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

    /**
     * Algorithme recursif parcourant l'arborescence et compare toutes les images entre elles selon leur nom.
     * Cette methode suppose que l'image EST dans le dossier donc ne renvoie pas d'erreur.
     * Cette methode n'est utile qu'au mode graphique car en cliquant sur un fichier de l'arborescence celui-ci est forcement dans l'arborescence.
     * @param name nom du fichier dont la recherche est l'objet.
     * @param fichiers ensemble des fichiers/dossiers du repertoire courant.
     * @param i itération de la recursivité.
     * @param parent repertoire parent (est renvoyé si la recherche resulte sur rien)
     * @return le fichier resultant de la recherche.
     */
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

    /**
     * Methode comparant la dimensions de deux images
     * @param x largeur de la 1ere image
     * @param y longueur de la 1ere images
     * @param im 2e image
     * @return un boolean vrai ou faux selon la comparaison
     * @throws IOException si l'extraction des dimensions de la 2e image échoue
     */
    private boolean verifDim(int x, int y, BufferedImage im) throws IOException {
        int[] dim = getDimension(im);
        return dim[0] >= x && dim[1] >= y;
    }

    /**
     * Methode comparant l'année de création de deux images
     * @param annee année de la 1ere image
     * @param f instance de la classe File relié à la 2e image
     * @return un bolléen vrai ou faux selon la comparaison
     * @throws IOException si l'extraction de l'année de création de la 2e image a échoué
     */
    private boolean verifAnnee(String annee, File f) throws IOException {
        String dateCreation = getCreationDate(f);
        String anneeFichier = dateCreation.substring(0,dateCreation.indexOf("-"));
        return anneeFichier.equals(annee);
    }

    /**
     * Methode qui extrait la dimension d'une image
     * @param image l'image dont il est question
     * @return la dimension de l'image sous forme de tableau
     */
    private int[] getDimension(BufferedImage image) {
        int largeur = image.getWidth();
        int hauteur = image.getHeight();
        return new int[]{largeur,hauteur};
    }

    /**
     * Compare deux String caractère par caractère comptant le nombre de caractère en commun.
     * @param var1 prermière variable
     * @param var2 deuxième variable
     * @return le nombre de caractère en commun
     */
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

    /**
     * Teste si un fichier est une image
     * @param p chemin du fichier
     * @return un booléen vrai ou faux selon le teste
     */
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
    /**
     * Teste si un fichier est une image.
     * Cette classe sert au mode graphique.
     * @return un booléen vrai ou faux selon le teste
     */
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

    /**
     * renvoie la date de création d'un fichier
     * @param f le fichier dont il est question
     * @return la date de création
     * @throws IOException si l'extraction de la date de création a échoué
     */
    public String getCreationDate(File f) throws IOException {
        return Files.getAttribute(f.toPath(),"creationTime").toString().substring(0,10);
    }

    /**
     * Renvoie le message de la console (resultat de l'analyse)
     * Cette methode sert uniquement au mode de recherche par deux critère
     * @return l'attribut consoleMessage
     */
    public String getConsoleMessage() {
        return consoleMessage;
    }

    /**
     * Renvoie le fichier trouvé (resultat de la recherche)
     * Cette methode sert uniquement au mod graphique.
     * @return l'attribut fileFound
     */
    public File getFileFound(){
        return fileFound;
    }

}
