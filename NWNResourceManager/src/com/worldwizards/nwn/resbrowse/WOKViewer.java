package com.worldwizards.nwn.resbrowse;

import com.worldwizards.nwn.files.resources.NWNText;
import com.worldwizards.nwn.files.resources.NWNWOK;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class WOKViewer
    extends JFrame {
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea jTextArea1 = new JTextArea();
  public WOKViewer(String name, NWNWOK wokFile) {
    super(name);
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    jTextArea1.setEditable(false);
    println("Parent: "+wokFile.getParent());
    float[] pos = wokFile.getPosition();
    println("Position:"+pos[0]+","+pos[1]+","+pos[2]);
    float[] orient = wokFile.getOrientation();
    println("Orientation: "+orient[0]+","+orient[1]+","+orient[2]);
    float[] wirecolor = wokFile.getWireColor();
    println("Wirecolor: "+wirecolor[0]+" "+wirecolor[1]+" "+wirecolor[2]);
    float[][] verts = wokFile.getVerts();
    println("Verts:");
    for(int i=0;i<verts.length;i++){
    	println("    "+verts[0]+","+verts[1]+","+verts[2]);
    	
    }
    pack();
    setVisible(true);
  }
  
  private void println(String line){
	  jTextArea1.append(line + "\n");
  }



  private void jbInit() throws Exception {
    jTextArea1.setText("");
    this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(jTextArea1, null);
  }
}
