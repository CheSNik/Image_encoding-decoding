import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Random;

public class Decoder {

    private String messageFromTheImage="";
    private ReadWriteBitmap imageReader;
    private RandomAccessFile newfinp;
    private int width;
    private int height;
    private int offset;
    private PrintWriter out;

    /***
     * Reads certain bytes in image and convert it to decoded message
     * @param filename reference type to a encrypted filename
     */
    public Decoder(String filename , PrintWriter out) {
        //using ReadWriteBitmap object not to make extra code for retrieving data from image
        this.out = out;
        out.println("Decoder at work...");
        imageReader = new ReadWriteBitmap(filename, out);
        width = imageReader.getWidth();
        height = imageReader.getHeight();
        offset = imageReader.getOffset();
        Random rnd = new Random();
        rnd.setSeed(Const.KEY);

        try {


            //instantiated new RandomAccessFile to open steam for reading
            newfinp = new RandomAccessFile(filename, "r");

            int messageLength = 0;

            newfinp.seek(offset);

            char b = (char)newfinp.read();
            char g = (char)newfinp.read();
            char r = (char)newfinp.read();

            newfinp.close();

            messageLength = (int)b + (int)g*256+ (int)r*256*256;
            out.println("Encoded message length is ="+messageLength);
            //messageLength=35;
            for (int i =0; i<messageLength;i++){
                int imagePosition = generateImagePosition(rnd);
                char[][] temp = generateRGBPosition(rnd);
                messageFromTheImage+= temp[imagePosition/width][imagePosition%width];
            }
            out.println("Message was decoded successfully");
            out.println("Decoded message is: "+messageFromTheImage);

        }
        catch (IOException e)
        {
            System.out.println("Error opening Bitmap file: " + filename);
        }

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
            case 0 : return imageReader.getBlue();
            case 1 : return imageReader.getGreen();
            case 2 : return imageReader.getRed();
            default: return null;
        }
    }
}
