package core;
import com.adobe.internal.xmp.XMPException;
import com.adobe.internal.xmp.XMPMeta;
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

public class Fichier implements EstAnalysable {
    private final Path chemin;
    private final File f;

    public Fichier(Path chemin) throws NoSuchFileException, WrongArgumentException {
        if(!Files.exists(chemin)){
            throw new NoSuchFileException("Chemin non existants, taper -h ou --help pour de l'aide");
        }
        this.chemin=chemin;
        f = new File(chemin.toString());
        if(f.isDirectory()){
            throw new WrongArgumentException(f.getName()+" est un repertoire, taper -d pour les manipuler \n" +
                    "taper -h ou --help pour de l'aide");
        }
    }

    public Fichier(File f) throws NoSuchFileException, WrongArgumentException {
        this.f=f;
        this.chemin=f.toPath();
        if(!Files.exists(chemin)){
            throw new NoSuchFileException("Chemin non existants, taper -h ou --help pour de l'aide");
        }
        if(f.isDirectory()){
            throw new WrongArgumentException(f.getName()+" est un repertoire, taper -d pour les manipuler \n" +
                    "taper -h ou --help pour de l'aide");
        }
    }

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

    public void printInfo() throws Exception {
        String nom = chemin.getFileName().toString();
        String type = nom.substring(nom.length() - 4);
        String type1 = nom.substring(nom.length() - 5);
        if (!(type.equals(".jpg") || type.equals(".png") || type1.equals(".webp") || type1.equals(".jpeg"))) {
            throw new WrongArgumentException("Le fichier entré n'est pas une image, taper -h ou --help pour de l'aide");
        }
        Metadata metadata = ImageMetadataReader.readMetadata(f);

        printExif(metadata);
        printOtherXMP(metadata);

        String a ="";
    }

    void printExif(Metadata m){
        ExifIFD0Directory exifIF = m.getFirstDirectoryOfType(ExifIFD0Directory.class);
        ExifSubIFDDirectory exifSUB = m.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        GpsDirectory gps = m.getFirstDirectoryOfType(GpsDirectory.class);
        String s="| Format : Exif |";
        String err="";
        if(exifIF!=null){
            String modelCam = exifIF.getString(ExifIFD0Directory.TAG_MODEL);
            String marqueCam = exifIF.getString(ExifIFD0Directory.TAG_MAKE);
            String resolutionX = exifIF.getString(ExifIFD0Directory.TAG_X_RESOLUTION);
            String resolutionY = exifIF.getString(ExifIFD0Directory.TAG_Y_RESOLUTION);
            s +=" Marque camera : "+marqueCam+" |"+ " Model : " + modelCam+" |";
            s+="Resolution X : "+resolutionX+"ppp, Y : "+resolutionY+"ppp |";
        }
        else{
            err = " Metadonne de la camera et de la resolution introuvable, ";
        }
        if(exifSUB!=null){
            String longeurFocale = exifSUB.getString(ExifSubIFDDirectory.TAG_FOCAL_LENGTH);
            String dureeExposition = exifSUB.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME);
            String ouvertureObjectif = exifSUB.getString(ExifSubIFDDirectory.TAG_FNUMBER);
            String sensibiliteISO = exifSUB.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT);
            String dateHeure = exifSUB.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            s+= "\n| Date et heure : "+dateHeure+" |";
            s+= " Duree d'exposition : "+dureeExposition+" |"+" Ouverture objectif : "
                    +ouvertureObjectif+" |"+" Sensibilite ISO : "+sensibiliteISO+" |"+" Longueur focale : "+longeurFocale+" |";
        }
        else{
            err+="parametre de prise de vue et d'heure introuvable, ";
        }
        if(gps!=null){
            String localisation = gps.getGeoLocation().toString();
            s+="\n| Coordonnee GPS :"+" "+localisation+" |\n";
        }
        else{
            err+="GPS introuvable";
        }
        System.out.println(s+err);
    }

    void printOtherXMP(Metadata m) throws XMPException {
        XmpDirectory xmp = m.getFirstDirectoryOfType(XmpDirectory.class);
        if(xmp!=null){
            XMPMeta xmpMeta = xmp.getXMPMeta();
            String s = "| Format : Dublin Core |";
            s+=" Titre : "+getXMPType(xmpMeta,"http://purl.org/dc/elements/1.1/","title")+" |";
            s+=" Createur : "+getXMPType(xmpMeta,"http://purl.org/dc/elements/1.1/","creator")+" |";
            s+=" Description : "+getXMPType(xmpMeta,"http://purl.org/dc/elements/1.1/","description")+" |";
            s+="\n| Format : IPTC Core |";
            s+=" Contact du createur : "+getXMPType(xmpMeta,"http://iptc.org/std/Iptc4xmpCore/1.0/xmlns/","CreatorContactInfo")+" |";
            s+=" Lieu : "+getXMPType(xmpMeta,"http://iptc.org/std/Iptc4xmpCore/1.0/xmlns/","Location")+" |";
            System.out.println(s);
        }
        else{
            System.out.println("Autres formats de metadonnee XMP introuvable");
        }
    }

    String getXMPType(XMPMeta xmp, String namescape, String tag) throws NullPointerException {
        String donnee="Metadonnee introuvable";
        try{
            donnee = String.valueOf(xmp.getLocalizedText(namescape,tag,null,"x-default"));
            if(donnee!=null){
                return donnee;
            }
        } catch (XMPException ignored) {
        }
        try{
            int i = xmp.countArrayItems(namescape,tag);
            if(i>0){
                String s= "";
                for(int a=0; a<i; a++){
                    s+=xmp.getArrayItem(namescape,tag,i).getValue();
                }
                return s;
            }
        } catch (XMPException ignored) {
        }
        try{
            donnee=xmp.getPropertyString(namescape,tag);
            return donnee;
        } catch (XMPException ignored) {
        }
        return donnee;
    }
}
