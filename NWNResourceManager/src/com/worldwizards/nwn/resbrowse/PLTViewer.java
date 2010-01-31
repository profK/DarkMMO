package com.worldwizards.nwn.resbrowse;

import com.worldwizards.nwn.files.resources.*;
import javax.swing.JFrame;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */



public class PLTViewer extends JFrame {
    ImagePanel img;
    BufferedImage[] palettes;
    NWNPLT plt;   
    final JComboBox[] colorCombos;
    /**
     * ImageViewer
     *
     * @param nWNImage NWNImage
     */
    public PLTViewer(BufferedImage[] palettes,
            String[] paletteNames, String imageName, NWNPLT plt) {
        super(imageName);
        this.plt = plt;
        this.palettes = palettes;
        byte[] colorIndexes = new byte[palettes.length];
        Arrays.fill(colorIndexes,(byte)32); // default
        BufferedImage bi = plt.getImage(palettes,colorIndexes);
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        img = new ImagePanel(bi);
        c.add(img ,BorderLayout.CENTER);
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(0,1));
        colorCombos = new JComboBox[colorIndexes.length];
        final Integer[] comboValues = new Integer[64];
        for(int i=0;i<comboValues.length;i++){
            comboValues[i] = new Integer(i);
        }
        for(int i=0;i<colorCombos.length;i++){
            JPanel selectorPanel = new JPanel();
            selectorPanel.setLayout(new GridLayout(1,0));
            colorCombos[i] = new JComboBox(comboValues);
            colorCombos[i].setSelectedIndex(colorIndexes[i]);
            colorCombos[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    updateImage();
                }
            });
            selectorPanel.add(colorCombos[i]);
            selectorPanel.add(new JLabel(paletteNames[i]));
            controlPanel.add(selectorPanel);
        }
        c.add(controlPanel,BorderLayout.EAST);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }
    
    private void updateImage(){
        byte[] paletteIndexes = new byte[colorCombos.length];
        for(int  i=0;i<paletteIndexes.length;i++){
            paletteIndexes[i] = (byte)colorCombos[i].getSelectedIndex();
        }
        BufferedImage bi = plt.getImage(palettes,paletteIndexes);
        img.updateImage(bi);
    }
    
}
