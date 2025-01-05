package core.cli;
import com.adobe.internal.xmp.XMPException;
import com.adobe.internal.xmp.XMPMeta;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.xmp.XmpDirectory;
import exception.WrongArgumentException;
import java.io.IOException;
import java.nio.file.*;
import java.io.File;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;

/**
 * Cette classe gere l'utilisation des fichiers entré par l'utilisateur
 * Elle permet d'obtenir ses statistiques et ses informations plus détaillés
 *
 * @author Dylan Manseri
 * @version 0.1
 */

public class Fichier {
    private final Path chemin;
    private final File f;
    private String consoleMessage;

    /**
     * Crée un instance de la classe Fichier et verifie si le fichier existe bien.
     * @param f le fichier sous forme d'objet de la classe File
     * @throws WrongArgumentException si le fichier entré est un repertoire et non un fichier, ou qu'il n'existe pas
     */
    public Fichier(File f) throws WrongArgumentException {
        this.f=f;
        this.chemin=f.toPath();
        if(!Files.exists(chemin)){
            throw new WrongArgumentException("Chemin non existants, taper -h ou --help pour de l'aide");
        }
        if(f.isDirectory()){
            throw new WrongArgumentException(f.getName()+" est un repertoire, taper -d pour les manipuler \n" +
                    "taper -h ou --help pour de l'aide");
        }
    }

    /**
     * Crée un instance de la classe Fichier et verifie si le fichier existe bien
     * @param chemin le chemin du fichier
     * @throws WrongArgumentException si le fichier et un repertoire ou n'existe pas
     */
    public Fichier(String chemin) throws WrongArgumentException {
        this.chemin= Paths.get(chemin).toAbsolutePath().normalize();
        f = new File(chemin);
        if(!Files.exists(this.chemin)){
            throw new WrongArgumentException("Chemin non existants, taper -h ou --help pour de l'aide");
        }
        if(f.isDirectory()){
            throw new WrongArgumentException(f.getName()+" est un repertoire, taper -d pour les manipuler " +
                    "taper -h ou --help pour de l'aide");
        }
    }

    /**
     * Ecrit les statistiques basiques du fichier instancié (date de création, derniere modification, taille).
     * @param ecrire Indique si les statistiques doivent être écrite ou non.
     * @return le message à écrire ou null si il ne faut rien écrire
     */
    public String printStat(boolean ecrire){
        try{
            String mere = chemin.getParent().getFileName().toString();
            String nom = chemin.getFileName().toString();
            String type = nom.substring(nom.length()-3);
            String mess=" est ";

            if(type.equals("txt") || type.equals("csv")){
                mess+="un fichier texte";
            }
            else if(type.equals("jpg") || type.equals("png") || type.equals("webp")){
                mess+="un fichier image";
            }
            String dateCreation = Files.getAttribute(chemin,"creationTime").toString().substring(0,10);
            String dateModification = Files.getLastModifiedTime(chemin).toString().substring(0,10);
            long taille = Files.size(chemin);

            String s="| Date de creation : "+dateCreation+" ";
            s+="| Date de la derniere modification : "+dateModification;
            s+="| Taille : "+taille+" octets |";
            mess+=" dans "+mere+"->"+s;
            if(ecrire){
                System.out.println(nom + " " + mess);
            }
            return mess;
        } catch(IOException e){
            System.out.println("erreur");
        }
        return null;
    }

    /**
     * Ecrit les statistiques basiques du fichier instancié (date de création, derniere modification, taille).
     * Cette methode sert au mode graphique
     * @return le message à écrire
     */
    public String printStatGui(){
        try{
            String mere = chemin.getParent().getFileName().toString();
            String nom = chemin.getFileName().toString();
            String type = nom.substring(nom.length()-4);
            String mess="est ";

            if(type.equals(".txt") || type.equals(".csv")){
                mess+="un fichier texte\n";
            }
            else if(type.equals(".jpg") || type.equals(".png") || type.equals("webp")){
                mess+="un fichier image\n";
            }
            else{
                mess="n'est pas une image\n";
            }
            String dateCreation = Files.getAttribute(chemin,"creationTime").toString().substring(0,10);
            String dateModification = Files.getLastModifiedTime(chemin).toString().substring(0,10);
            long taille = Files.size(chemin);

            String s="Date de creation : "+dateCreation+"\n";
            s+="Date de la derniere modification : "+dateModification+"\n";
            s+="Taille : "+taille+" octets";
            mess+="Dossier parent : "+mere+"\n"+s;
            return nom+" "+mess;
        } catch(IOException e){
            System.out.println("erreur");
        }
        return null;
    }

    /**
     * Centralise l'extraction des métadonnnés d'un fichier et stocke le message associé dans consoleMessage.
     * Cette methode appele deux autre methode dédié à un type de metadonnée.
     * @throws Exception si le fichier n'est pas une image
     */
    public void printInfo() throws Exception {
        String nom = chemin.getFileName().toString();
        String type = nom.substring(nom.length() - 4);
        String type1 = nom.substring(nom.length() - 5);
        if (!(type.equals(".jpg") || type.equals(".png") || type1.equals(".webp") || type1.equals(".jpeg"))) {
            throw new WrongArgumentException("Le fichier entre n'est pas une image, taper -h ou --help pour de l'aide");
        }
        Metadata metadata = ImageMetadataReader.readMetadata(f);

        consoleMessage = printExif(metadata);
        consoleMessage += printOtherXMP(metadata);
    }

    /**
     * Extrait les metadonnées de format EXIF
     * @param m les metadonnés (objet de classe Metadata) associé au fichier.
     * @return le message associé
     */
    public String printExif(Metadata m){
        ExifIFD0Directory exifIF = m.getFirstDirectoryOfType(ExifIFD0Directory.class);
        ExifSubIFDDirectory exifSUB = m.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        GpsDirectory gps = m.getFirstDirectoryOfType(GpsDirectory.class);
        String s="| Format : Exif |\n";
        String err="";
        if(exifIF!=null){
            String modelCam = exifIF.getString(ExifIFD0Directory.TAG_MODEL);
            String marqueCam = exifIF.getString(ExifIFD0Directory.TAG_MAKE);
            String resolutionX = exifIF.getString(ExifIFD0Directory.TAG_X_RESOLUTION);
            String resolutionY = exifIF.getString(ExifIFD0Directory.TAG_Y_RESOLUTION);
            s +="Marque camera : "+marqueCam+"\n"+ "Modele : " + modelCam+"\n";
            s+="Resolution X : "+resolutionX+"ppp, Y : "+resolutionY+"ppp\n";
        }
        else{
            err = "Metadonne de la camera et de la resolution introuvable, \n";
        }
        if(exifSUB!=null){
            String longeurFocale = exifSUB.getString(ExifSubIFDDirectory.TAG_FOCAL_LENGTH);
            String dureeExposition = exifSUB.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME);
            String ouvertureObjectif = exifSUB.getString(ExifSubIFDDirectory.TAG_FNUMBER);
            String sensibiliteISO = exifSUB.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT);
            String dateHeure = exifSUB.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            s+= "Date et heure : "+dateHeure+" \n";
            s+= "Duree d'exposition : "+dureeExposition+"\n"+"Ouverture objectif : "
                    +ouvertureObjectif+"\n"+"Sensibilite ISO : "+sensibiliteISO+"\n"+"Longueur focale : "+longeurFocale+"\n";
        }
        else{
            err+="parametre de prise de vue et d'heure introuvable, \n";
        }
        if(gps!=null){
            String localisation = gps.getGeoLocation().toString();
            s+="Coordonnee GPS :"+" "+localisation+"\n";
        }
        else{
            err+="GPS introuvable\n";
        }
        return s+err;
    }

    /**
     * Extrait les metadonnées de format EXIF
     * @param m les metadonnés (objet de classe Metadata) associé au fichier.
     * @return le message associé
     */
    public String printOtherXMP(Metadata m) {
        XmpDirectory xmp = m.getFirstDirectoryOfType(XmpDirectory.class);
        if(xmp!=null){
            XMPMeta xmpMeta = xmp.getXMPMeta();
            String s = "\n| Format : Dublin Core |\n";
            s+="Titre : "+getXMPType(xmpMeta,"http://purl.org/dc/elements/1.1/","title")+"\n";
            s+="Createur : "+getXMPType(xmpMeta,"http://purl.org/dc/elements/1.1/","creator")+"\n";
            s+="Description : "+getXMPType(xmpMeta,"http://purl.org/dc/elements/1.1/","description")+"\n";
            s+="\n| Format : IPTC Core |";
            s+="\nContact du createur : "+getXMPType(xmpMeta,"http://iptc.org/std/Iptc4xmpCore/1.0/xmlns/","CreatorContactInfo")+"\n";
            s+="Lieu : "+getXMPType(xmpMeta,"http://iptc.org/std/Iptc4xmpCore/1.0/xmlns/","Location")+"\n";
            return s;
        }
        else{
            return "Autres formats de metadonnee XMP introuvable";
        }
    }

    /**
     * Cette classe force 3 type d'analyse visant déterminé de quelle façon une metadonnée est écrite
     * @param xmp La metadonnée XMP a analyser
     * @param namespace le nom de la namespace de la metadonnée
     * @param tag le nom de la metadonnée (date, creation...)
     * @return le resultat de l'analyse (soit les metadonnees, soit un message d'erreur)
     * @throws NullPointerException si l'extraction a echoué mais ne fait rien pour pouvoir tester
     * les autres types, en cas d'erreur dans toutes les analyse "Metadonnee introuvable" est renvoyé
     */
    private String getXMPType(XMPMeta xmp, String namespace, String tag) throws NullPointerException {
        String donnee="Metadonnee introuvable";
        try{
            donnee = String.valueOf(xmp.getLocalizedText(namespace,tag,null,"x-default"));
            if(donnee!=null){
                return donnee;
            }
        } catch (XMPException ignored) {
        }
        try{
            int i = xmp.countArrayItems(namespace,tag);
            if(i>0){
                StringBuilder s= new StringBuilder();
                for(int a=0; a<i; a++){
                    s.append(xmp.getArrayItem(namespace, tag, i).getValue());
                }
                return s.toString();
            }
        } catch (XMPException ignored) {
        }
        try{
            donnee=xmp.getPropertyString(namespace,tag);
            return donnee;
        } catch (XMPException ignored) {
        }
        return donnee;
    }

    /**
     * @return le message de la console à propos de l'analyse
     */
    public String getConsoleMessage() {
        return consoleMessage;
    }
}
