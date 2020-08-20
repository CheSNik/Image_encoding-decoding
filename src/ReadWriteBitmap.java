import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;


/**
 * This class provide encoding and decoding functions to store and retrieve string from *BMP image
 * @author Sergei Chekhonin
 * @version 1.0 08/02/2020
 */
public class ReadWriteBitmap
{

    private RandomAccessFile finp;
    private int ifilesize;
    private int ioffset;
    private int iwidth;
    private int iheight;
    private int ipadding;
    private byte [] header;
    private char [][] bpixels;
    private char [][] gpixels;
    private char [][] rpixels;
    private PrintWriter out;


    public ReadWriteBitmap(String filename, PrintWriter out)
    {
        this.out = out;
        try
        {
            finp = new RandomAccessFile(filename, "r");

            ifilesize = get_info(2);
            ioffset = get_info(10);
            iwidth = get_info(18);
            iheight = get_info(22);

            ipadding = 4 - ((3 * iwidth) % 4);
            ipadding = ipadding % 4;

            // read header
            header = new byte[ioffset]; //offset is not only beginning of BGR but also and length of Header
            finp.seek(0);
            finp.read(header);

//            int n = (int)(header[18]) + (int)(header[19])*256 +
//                (int)(header[20])*256*256 + (int)(header[21])*256*256*256;

            out.println("File was found");
            out.println("Image size is " + ifilesize);
            out.println("Image width is " + iwidth);
            out.println("Image height is " + iheight);

            readImage();

            finp.close();
        }
        catch (IOException e)
        {
            System.out.println("Error opening Bitmap file: " + filename);
        }
    }

    /**
     * Reads 4 bytes from specific position in header and stores values in Int variables
     * @param position starting specific position for reading 4 bytes
     * @return value stored in 4 specific bytes
     */
    private int get_info(int position)
    {
        int sum=-1;
        int byteoffset = 1;

        try
        {
            sum = 0;
            finp.seek(position);

            for (int i = 0; i < 4; i++)
            {
                int n = finp.read();

                sum += n * byteoffset;
                byteoffset *= 256;
            }
        }
        catch (IOException e)
        {
            System.out.println("Error reading file");
        }

        return sum;
    }

    /**
     * Retrieve file size
     * @return file size
     */
    public int getFilesize()
    {
        return ifilesize;
    }
    /**
     * Retrieve starting address of the image RBG data
     * @return starting address of the image RBG data
     */
    public int getOffset()
    {
        return ioffset;
    }

    /**
     * Retrieve width of the image
     * @return width of the image in Pixels
     */
    public int getWidth()
    {
        return iwidth;
    }

    /**
     * Retrieve height of the image
     * @return height of the image in Pixels
     */
    public int getHeight()
    {
        return iheight;
    }

    /**
     * Creates new file usin new header from "header" variable and image data from bpixels, gpixels, rpixels
     * @param filename name of output file
     * @param B array of char type bytes for 'b' pixel position
     * @param G array of char type bytes for 'g' pixel position
     * @param R array of char type bytes for 'r' pixel position
     */
    public void writeImage(String filename, char [][]B, char [][]G, char [][]R)
    {
        try
        {

            RandomAccessFile fout = new RandomAccessFile(filename, "rw");

            fout.write(header);

            for (int w = 0; w < iheight; w++)
            {
                for (int h = 0; h < iwidth; h++)
                {
                    fout.write((int)B[w][h]);
                    fout.write((int)G[w][h]);
                    fout.write((int)R[w][h]);
                }
                for (int i = 0; i < ipadding; i++)
                {
                    fout.write((int)(' '));
                }
            }

            fout.close();

        }
        catch (IOException e)
        {
            System.out.println("Error opening file for output");
        }

    }

    /**
     * Reads image data and stores each pixel byte to an array
     */
    public void readImage()
    {
        try
        {
            finp.seek(ioffset);

            bpixels = new char[iheight][iwidth];
            gpixels = new char[iheight][iwidth];
            rpixels = new char[iheight][iwidth];

            for (int w = 0; w < iheight; w++)
            {
                for (int h = 0; h < iwidth; h++)
                {
                    char b = (char)finp.read();
                    char g = (char)finp.read();
                    char r = (char)finp.read();

                    bpixels[w][h] = b;
                    gpixels[w][h] = g;
                    rpixels[w][h] = r;
                }

                finp.skipBytes(ipadding);

            }
        }
        catch (IOException e)
        {
            System.out.println("Error reading file");
        }

    }

    /**
     * retrieve data from bpixel array
     * @return array of bpixel
     */
    public char [][] getBlue()
    {
        return bpixels;
    }
    /**
     * retrieve data from gpixel array
     * @return array of gpixel
     */
    public char [][] getGreen()
    {
        return gpixels;
    }
    /**
     * retrieve data from rpixel array
     * @return array of rpixel
     */
    public char [][] getRed()
    {
        return rpixels;
    }
}
