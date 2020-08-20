import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Lab 4 This program encodes and decodes image
 * @author Sergei Chekhonin
 */
public class Main {

    /***
     * Main - enter point to the program
     */
    public static void main(String[] args) {
        //This block initialize printWriter and dateFormatter
        PrintWriter out = null;
        try {
            out = new PrintWriter("Lab4_output.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        out.println(dtf.format(now));

        ReadWriteBitmap myImage = new ReadWriteBitmap("rainbow2.bmp", out);
        char [][] B = myImage.getBlue();
        char [][] G = myImage.getGreen();
        char [][] R = myImage.getRed();

        //encodes image
        new Encoder(myImage,out);

        myImage.writeImage("rainbow_encrypt.bmp", B, G, R);

        System.out.println(myImage.getFilesize());
        System.out.println(myImage.getOffset());
        System.out.println(myImage.getWidth());
        System.out.println(myImage.getHeight());

        //decodes image and returns hidden message as a string
        Decoder decoder = new Decoder("rainbow_encrypt.bmp", out);

        out.close();
        System.exit(0);

    }

}





