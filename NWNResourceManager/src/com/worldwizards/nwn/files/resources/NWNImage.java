package com.worldwizards.nwn.files.resources;

import com.worldwizards.nwn.files.NWNResource;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import java.io.*;
import java.nio.ByteBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferByte;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.DataBuffer;
import java.awt.Point;
import java.awt.image.DirectColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.Transparency;
import java.awt.color.ColorSpace;

public class NWNImage
    extends NWNResource {
  ByteBuffer myBuff;
  private boolean upsidedown = true; //TGAs are stored upside down

  public static void register() {
    NWNResource.registerHandler(1, "bmp", NWNImage.class);
    NWNResource.registerHandler(3, "tga", NWNImage.class);
    NWNResource.registerHandler(2033, "dds", NWNImage.class);
  }

  public NWNImage(String name, short type, ByteBuffer buff) {
    super(name, type);
    myBuff = buff.slice();
    myBuff.order(buff.order());
  }

  public boolean isUpsidedown() {
    return upsidedown;
  }
  
  public ByteBuffer getByteBuffer(){
	  return myBuff;
  }

  /**
   * getImage
   *
   * @return BufferedImage
   */
  public BufferedImage getImage() {
    if (getType() == 3) { // tga
      myBuff.rewind();
      byte idLength = myBuff.get();
      byte colorMapType = myBuff.get();
      byte imageType = myBuff.get();
      short colorMapStart = myBuff.getShort();
      short colorMapLength = myBuff.getShort();
      byte bitsPerColorMapEntry = myBuff.get();
      short xOrigin = myBuff.getShort();
      short yOrigin = myBuff.getShort();
      short imageWidth = myBuff.getShort();
      short imageHeight = myBuff.getShort();
      byte bitsPerPixel = myBuff.get();
      byte imageDescriptor = myBuff.get();
      byte[] imageColorMapID = null;
      if (idLength > 0) {
        imageColorMapID = new byte[idLength];
        myBuff.get(imageColorMapID);
      }
      // header done, load data
      int bytesPerPixel = (int) Math.ceil(bitsPerPixel / 8.00);
      byte[] data = new byte[imageWidth * bytesPerPixel * imageHeight];
      byte[] pix = new byte[bytesPerPixel];
      if ( (imageType & 8) == 8) { // rle
        for (int pp = 0; pp < pix.length; ) {
          byte rep = myBuff.get();
          int sz = (rep & 0x7F);
          if ( (rep & 0x80) == 0x80) { //rle packet
            myBuff.get(pix);
            for (int i = 0; i < sz; i++) {
              System.arraycopy(pix, 0, data, pp, pix.length);
              pp += pix.length;
            }
          }
          else { // raw packet
            myBuff.get(data, pp, sz * bytesPerPixel);
            pp += (sz * bytesPerPixel);
          }
        }
      }
      else {
        myBuff.get(data);
      }
      // now turn data inot a buffered image
      DataBufferByte dbb = new DataBufferByte(data, data.length);

      switch (bytesPerPixel) {
        case 3:
          WritableRaster raster = Raster.createInterleavedRaster(dbb,
              imageWidth,
              imageHeight, imageWidth * bytesPerPixel, bytesPerPixel,
              new int[] {2, 1, 0}
              ,
              new Point(xOrigin, yOrigin));
          return new BufferedImage(
              new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                      new int[] {8, 8, 8, 0}
                                      ,
                                      false,
                                      false,
                                      ComponentColorModel.OPAQUE,
                                      DataBuffer.TYPE_BYTE)
              , raster, false, null)
              ;

        case 4:
          raster = Raster.createInterleavedRaster(dbb,
                                                  imageWidth,
                                                  imageHeight,
                                                  imageWidth * bytesPerPixel,
                                                  bytesPerPixel,
                                                  new int[] {2, 1, 0, 3}
                                                  ,
                                                  new Point(xOrigin, yOrigin));
          return new BufferedImage(
              new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                      new int[] {8, 8, 8, 8}
                                      ,
                                      true,
                                      false,
                                      ComponentColorModel.TRANSLUCENT,
                                      DataBuffer.TYPE_BYTE)

              , raster, false, null)
              ;

        default:
          System.out.println(
              "ERROR: Usupported data type for bytes per pixel = " +
              bytesPerPixel);
          return null;
      }

      // new Point(xOrigin,yOrigin));
    }

    return null;
  }
  
}
