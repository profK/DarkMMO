package com.worldwizards.nwn.resbrowse;

import com.worldwizards.nwn.files.resources.NWNText;
import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class TextViewer
    extends JFrame {
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea jTextArea1 = new JTextArea();
  public TextViewer(String name, NWNText textResource) {
    super(name);
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    for (Iterator i = textResource.getTextLines().iterator(); i.hasNext(); ) {
      String line = (String) i.next();
      jTextArea1.append(line + "\n");
    }
    pack();
    setVisible(true);
  }



  private void jbInit() throws Exception {
    jTextArea1.setText("");
    this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(jTextArea1, null);
  }
}
