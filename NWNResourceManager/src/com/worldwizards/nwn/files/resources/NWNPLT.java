package com.worldwizards.nwn.files.resources;

import com.worldwizards.nwn.files.NWNResource;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class NWNPLT
        extends NWNResource {
    private boolean upsidedown = true; //TGAs are stored upside down
    int imgWidth;
    int imgHeight;
    byte[][][] data;
    
    public static void register() {
        NWNResource.registerHandler(6, "plt", NWNPLT.class);
    }
    
    public NWNPLT(String name, short type, ByteBuffer buff) {
        super(name, type);
        ByteBuffer myBuff = buff.slice();
        myBuff.order(buff.order());
        byte[] signiture = new byte[8];
        myBuff.get(signiture); // shoudl be cheked, for now be sloppy & ignore
        myBuff.position(myBuff.position()+8);
        imgWidth = myBuff.getInt();
        imgHeight = myBuff.getInt();
        data = new byte[imgWidth][imgHeight][2];
        for(int y=0;y<imgHeight;y++){
            for (int x=0;x<imgWidth;x++){
                data[x][y][0] = myBuff.get();
                data[x][y][1] = myBuff.get();
            }
        }
    }
    
    public boolean isUpsidedown() {
        return upsidedown;
    }
    
    /**
     * getImage
     *
     * @return BufferedImage
     */
    public BufferedImage getImage(BufferedImage[] palettes,
            byte[] paletteIndexes) {
        BufferedImage image =
                new BufferedImage(imgWidth,imgHeight,
                BufferedImage.TYPE_INT_ARGB);
        for(int y=0;y<imgHeight;y++){
            for (int x=0;x<imgWidth;x++){
                int colorIndex = ((int)data[x][y][0])&0xff;
                int groupIndex = ((int)data[x][y][1])&0xff;
                image.setRGB(x,y,palettes[groupIndex].getRGB(colorIndex, 
                        paletteIndexes[groupIndex]));
            }
        }
        return image;
    }   
    
    public BufferedImage getImage(BufferedImage[] palettes){
        byte[] pindexes = new byte[palettes.length];
        Arrays.fill(pindexes,(byte)32);
        return getImage(palettes,pindexes);
    }
    
}
