package lostcassowary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Lawnguy
 */
public class Region extends FileHandling 
{

    private String fileRegionParsedx;
    private String fileRegionparsedz;
    private int xCord;
    private int zCord;
    private int x;
    private int z;
    private int regionX;
    private int regionZ;
    private final List<Byte> chunkByteLocations = new ArrayList<>();
    private final List<Byte> chunkTimeStamps = new ArrayList<>();
    private final List<Integer> chunkByteBigEndian = new ArrayList<>();

    /**
     * The getRegionCords method finds the appropriate region coordinate from a
     * set of normal Minecraft coordinates
     *
     * @return the x-coordinate and the Z-coordinate of the region
     */
    public int[] getRegionCords() 
    {
        regionX = (int) Math.floor(xCord / 32.0f);
        regionZ = (int) Math.floor(zCord / 32.0f);

        return new int[]{regionX, regionZ};

    }

    /**
     * The setRegionCords takes in the info from the demo file to be used in the
     * getRegionCords method
     *
     * @param startingXCord asks user to enter an x coordinate from the
     * Minecraft world
     * @param startingZCord ask user to enter an z coordinate from the Minecraft
     * world
     */
    public void setRegionCords(int startingXCord, int startingZCord) 
    {

        xCord = startingXCord;
        zCord = startingZCord;

    }

    /**
     * The setChunkLocations takes the user input and sends it back to the
     * setPath method in FileHadling class
     *
     * @param initalFilePath users path to the directory where the region files
     * are held
     */
    public void setChunkLocations(String initalFilePath)
    {
        super.setFilePath(initalFilePath);
    }

    /** 
     * The getChunkLocations reads the first 4 bytes 1024 times in each file 
     * and stores it in an arrayList 
     *
     * @return chunkByteLocations the array that each element is one byte 
     * of the 4
     * @throws java.io.FileNotFoundException throws null error when there 
     * are not files 
     * @throws IOException
     */
   public List<Byte> getChunkLocations() throws FileNotFoundException, IOException 
{
    // Locations (1024 entries; 4 bytes each)
    
    Object[] filenames = getFiles().toArray();
    System.out.println(filenames.length);

    for (int i = 0; i < filenames.length; i++) 
    {
        byte[] b = new byte[4096]; 

        try (FileInputStream fileName = new FileInputStream((File) filenames[i])) 
        {
            fileName.read(b); 

            for (int j = 0; j < 1024; j++) 
            {
                int start = j * 4; 
                
                int bigEndianInt = ((b[start] & 0xFF) << 16) |
                                   ((b[start + 1] & 0xFF) << 8) |
                                   (b[start + 2] & 0xFF);
                
               chunkByteBigEndian.add(bigEndianInt);

                chunkByteLocations.add(b[start]);
                chunkByteLocations.add(b[start + 1]);
                chunkByteLocations.add(b[start + 2]);

                chunkByteLocations.add(b[start + 3]);
            }
        }
    }
    return chunkByteLocations;
}
   public List<Integer> getChunkLocationOffset()
   {
       return chunkByteBigEndian;
   }
   
   
   
    /**
     * The getChunkTimeStamps method gets the first 4 bytes of each file after 
     * 4096 bytes and saves the data into and ArrayList
     * 
     * @return chunkTimeStamps the array that holds the data, each element 
     * is one byte
     * @throws FileNotFoundException throws a null when there is not file in 
     * the directory 
     * @throws IOException
     */
    public List<Byte> getChunkTimeStamps() throws FileNotFoundException, 
            IOException 
    {
          //locations (1024 entries; 4 bytes each)
        
        Object[] filename = getFiles().toArray();
        System.out.println(filename.length);

        for (int i = 0; i < filename.length; i++) 
        {
            byte[] a = new byte[1024 * 4];

            try (FileInputStream fileNames = new FileInputStream((File) filename[i])) 
            {
                int cursor = 0;
                
                fileNames.skip(4096);

                for (int j = 0; j < 1024; j++) 
                {
                    fileNames.read(a, cursor, 4);
                    cursor += 4;
                }
            }
            for (int k = 0; k < a.length; k++)
            {
                chunkTimeStamps.add(a[k]);
            }
            //System.out.println(chunkTimeStamps);
        }
        return chunkTimeStamps;

    }

    public byte getChunksAndOther() 
    {

    }

   

}
