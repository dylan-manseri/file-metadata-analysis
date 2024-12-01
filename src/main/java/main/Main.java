package main;
import classe.Arguments;
import com.drew.imaging.ImageProcessingException;
import exception.TooMuchArgumentsException;
import exception.WrongArgumentException;
import java.io.IOException;

//classe constructeur methode

public class Main {
    public static void main(String[] args) throws WrongArgumentException, TooMuchArgumentsException, IOException{
        try{
            if(args.length>5){
                throw new TooMuchArgumentsException();
            }
            Arguments a = new Arguments(args);
        } catch(TooMuchArgumentsException | WrongArgumentException | IOException e){
            System.out.println(e.getMessage());
        } catch (ImageProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}