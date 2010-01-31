package com.worldwizards.nwn.resbrowse;

import java.awt.*;
import javax.swing.*;
import com.worldwizards.nwn.files.resources.Win32INI;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.swing.tree.TreeModel;
import java.util.Map;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;



public class WinINIViewer extends JFrame{
  BorderLayout borderLayout1 = new BorderLayout();
  JTree iniTree;
  JScrollPane jScrollPane1 = new JScrollPane();

  public WinINIViewer(String name, Win32INI iniRes) {
    try {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode(name);
      for(Iterator i= iniRes.getParagraphIterator();i.hasNext();){
        Entry entry = (Entry)i.next();
        DefaultMutableTreeNode pnode =
            new DefaultMutableTreeNode((String)entry.getKey());
        root.add(pnode);
        Map pmap = (Map)entry.getValue();
        for (Iterator i2 = pmap.entrySet().iterator();i2.hasNext();){
          Entry entry2 = (Entry)i2.next();
          DefaultMutableTreeNode enode =
              new DefaultMutableTreeNode((String)entry2.getKey()+
                                         "="+(String)entry2.getValue());
          pnode.add(enode);
        }
      }
      iniTree = new JTree(root);
      iniTree.setEditable(false); // for the moment
      jbInit();
      pack();
      setVisible(true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    this.getContentPane().setLayout(borderLayout1);
    this.getContentPane().add(jScrollPane1,  BorderLayout.CENTER);
    jScrollPane1.getViewport().add(iniTree, null);
  }
}
