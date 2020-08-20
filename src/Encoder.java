import java.io.PrintWriter;
import java.util.Random;

public class Encoder {

    private char [][] bpixels;
    private char [][] gpixels;
    private char [][] rpixels;
    private int width;
    private int height;
    private int offset;
    private PrintWriter out;

    /**
     * Change data image to an encrypted data
     * @param myImage reference to ReadWriteBitmap object instantiated in Main.
     */
    public Encoder(ReadWriteBitmap myImage, PrintWriter out) {

        bpixels = myImage.getBlue();
        gpixels = myImage.getGreen();
        rpixels = myImage.getRed();
        width = myImage.getWidth();
        height = myImage.getHeight();
        offset = myImage.getOffset();
        this.out = out;
        stringEncoder();
    }

    /**
     * Retrieve length of message to encrypt
     * @return length of message to encrypt
     */
    private int getMessageLength(){
        return Const.stringToTheImage.length();
    }
    /***
     * Change data in certain bytes of bpixels, gpixels, rpixels arrays (from ReadWriteBitmap object) to a data from message
     */
    private void stringEncoder()
    {
        out.println("Encoder at work....");
        Random rnd = new Random();
        rnd.setSeed(Const.KEY);
        int b=0,g=0,r=0;
        b= getMessageLength();
        if (b<256) {
            bpixels[0][0] = (char) b;
            gpixels[0][0] = (char) g;
            rpixels[0][0] = (char) r;
        }
        else if (b<256*256) {
            g=b/256;
            b=b%256;
            bpixels[0][0] = (char) b;
            gpixels[0][0] = (char)g;
            rpixels[0][0] = (char)r;
        }
        else if (b<256*256*256){
            r=b/(256*256);
            g=b/256;
            b=b%256;
            bpixels[0][0] = (char) b;
            gpixels[0][0] = (char)g;
            rpixels[0][0] = (char)r;
        }

        for (int i =0; i<getMessageLength();i++){
            int imagePosition = generateImagePosition(rnd);
            char[][] temp = generateRGBPosition(rnd);
            temp[imagePosition/width][imagePosition%width] = Const.stringToTheImage.charAt(i);
            }
        out.println("Image was encoded successfully");
    }

    /**
     * Returns pseudo random position to locate encrypted data limited with number of pixels
     * @param random reference to the Random object
     * @return pseudo random number
     */
    private int generateImagePosition(Random random){
        return random.nextInt(width*height-1)+1;
    }

    /**
     * Returns pseudo random number of array to locate encrypted data limited with number of arrays
     * @param random eference to the Random object
     * @return pseudo random number
     */
    private char[][] generateRGBPosition(Random random){
        int i =random.nextInt(3);
        switch (i){
            case 0 : return bpixels;
            case 1 : return gpixels;
            case 2 : return rpixels;
            default: return null;
        }
    }


}
