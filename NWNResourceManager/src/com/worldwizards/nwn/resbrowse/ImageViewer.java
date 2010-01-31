package com.worldwizards.nwn.resbrowse;

import com.worldwizards.nwn.files.resources.*;
import javax.swing.JFrame;
import java.awt.Container;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

class ImagePanel extends JPanel {
  BufferedImage img;
  float zoom = 3.0f;
  boolean upsidedown = true;
  
   public ImagePanel(BufferedImage i){
       this(i,false);
   }
  
  public ImagePanel(BufferedImage i, boolean isUpsidedown){
    img = i;
    upsidedown = isUpsidedown;
  }

  public void paint(Graphics g){
    super.paint(g);
    Graphics2D g2d = (Graphics2D)g;
    
    if (upsidedown){
       g2d.translate(0,img.getHeight()*zoom); 
       g2d.scale(zoom,-zoom);
    }else {
       g2d.scale(zoom,zoom);
    }
    g2d.drawRenderedImage(img,null);
  }

  public Dimension getPreferredSize(){
    return new Dimension((int)(img.getWidth(this)*zoom),
            (int)(img.getHeight(this)*zoom));
  }
  
  public void updateImage(BufferedImage i){
      img = i;
      this.setSize(getPreferredSize());
      this.repaint();
  }
}

public class ImageViewer extends JFrame {

  static {
    String[] formats = ImageIO.getReaderFormatNames();
    System.out.println("Understood image types: ");
    for(int  i=0;i<formats.length;i++){
      System.out.println(formats[i]);
    }
  }
  /**
   * ImageViewer
   *
   * @param imageName 
   * @param nWNImage NWNImage
   */
  public ImageViewer(String imageName, NWNImage nWNImage) {
    super(imageName);
    BufferedImage bi = nWNImage.getImage();
    if (bi == null) {
      throw new InstantiationError("Image type not supported or bad image data!");
    }
    Container c = getContentPane();
    c.setLayout(new BorderLayout());
    c.add(new ImagePanel(bi,nWNImage.isUpsidedown()),BorderLayout.CENTER);
    System.out.println(bi.getWidth(null)+"<"+bi.getHeight(null));
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    pack();
    setVisible(true);
  }



}
