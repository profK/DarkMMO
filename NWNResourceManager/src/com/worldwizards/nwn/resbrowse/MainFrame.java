package com.worldwizards.nwn.resbrowse;

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.*;

import com.worldwizards.nwn.*;
import com.worldwizards.nwn.files.*;
import com.worldwizards.nwn.files.resources.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

class BrowserTableModel
    extends AbstractTableModel {
  ResourceDescriptor[] rd;
  private String[] columnNames = new String[] {
      "ResRef", "Extension", "Res Type"};  

  public BrowserTableModel(ResourceDescriptor[] rd) {
    super();
    this.rd = rd;
    sortBy(0);
  }

  /**
   * getColumnCount
   *
   * @return int
   */
  public int getColumnCount() {
    return 3;
  }

  /**
   * getRowCount
   *
   * @return int
   */
  public int getRowCount() {
    return rd.length;
  }

  /**
   * getValueAt
   *
   * @param rowIndex int
   * @param columnIndex int
   * @return Object
   */
  public Object getValueAt(int rowIndex, int columnIndex) {
    switch (columnIndex) {
      case 0:
        return rd[rowIndex].getResRef();
      case 1:
        return rd[rowIndex].getExt();
      case 2:
        return new Integer(rd[rowIndex].getResType());
    }
    return null;
  }

  public String getColumnName(int col) {
    return columnNames[col];
  }

  public ResourceDescriptor getRowRD(int index) {
    return rd[index];
  }

  public void sortBy(int columnIndex) {
    Comparator c = null;
    switch (columnIndex) {
      case 0:
        c = new Comparator() {
          public int compare(Object o1, Object o2) {
            ResourceDescriptor rd1 = (ResourceDescriptor) o1;
            ResourceDescriptor rd2 = (ResourceDescriptor) o2;
            return rd1.getResRef().compareTo(rd2.getResRef());
          }
        };
        break;
      case 1:
        c = new Comparator() {
          public int compare(Object o1, Object o2) {
            ResourceDescriptor rd1 = (ResourceDescriptor) o1;
            ResourceDescriptor rd2 = (ResourceDescriptor) o2;
            return rd1.getExt().compareTo(rd2.getExt());
          }
        };
        break;
      case 2:
        c = new Comparator() {
          public int compare(Object o1, Object o2) {
            ResourceDescriptor rd1 = (ResourceDescriptor) o1;
            ResourceDescriptor rd2 = (ResourceDescriptor) o2;
            int rt1 = rd1.getResType();
            int rt2 = rd2.getResType();
            if (rt1 < rt2) {
              return -1;
            }
            else if (rt1 > rt2) {
              return 1;
            }
            else {
              return 0;
            }
          }
        };
        break;
    }
    Arrays.sort(rd, c);
    this.fireTableDataChanged();
  }

}

public class MainFrame
    extends JFrame {
  ResourceManager mgr = new ResourceManager();
  private BufferedImage[] paletteGroups;    
  static String[] paletteGroupNames = 
    {"pal_skin01","pal_hair01","pal_armor01","pal_armor02",
     "pal_cloth01","pal_cloth01","pal_leath01","pal_leath01",
     "pal_tattoo01","pal_tattoo01"};       
  //GUI Stuff
  JPanel contentPane;
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenuFile = new JMenu();
  JMenuItem jMenuFileExit = new JMenuItem();
  JMenu jMenuHelp = new JMenu();
  JMenuItem jMenuHelpAbout = new JMenuItem();
  JToolBar jToolBar = new JToolBar();
  JButton jButton1 = new JButton();
  JButton jButton2 = new JButton();
  JButton jButton3 = new JButton();
  JButton jButton4 = new JButton();
  ImageIcon image1;
  ImageIcon image2;
  ImageIcon image3;
  JLabel statusBar = new JLabel();
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTable jTable1 = new JTable();
  TableModel tableModel1 = new DefaultTableModel();
  JButton jButton5 = new JButton();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  String[] comboSelections = {
      "Sort by ResRef", "Sort by Extension",
      "Sort by Res Type"};
  JComboBox jComboBox1 = new JComboBox(comboSelections);
  BrowserTableModel mdl;
  JFileChooser modFileChooser = new JFileChooser();
  //Construct the frame
  public MainFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
      // set up resource manager
      // do key tables
      File dir = new File(System.getProperty("nwndir",System.getProperty("user.dir")));
      File[] keyfiles = dir.listFiles(new FilenameFilter() {
        public boolean accept(File dir, String name) {
          return name.endsWith(".key");
        }
      });
      for (int i = 0; i < keyfiles.length; i++) {
        mgr.addKeyTable(
            new KeyTable(new FileInputStream(keyfiles[i]).getChannel()));
      }
      Set descrSet = mgr.listResources();
      final ResourceDescriptor[] rda =
          (ResourceDescriptor[]) (descrSet.toArray(
          new ResourceDescriptor[descrSet.size()]));
      mdl = new BrowserTableModel(rda);
      jTable1.setModel(mdl);
      updateStatus();
      // Now set up palette groups
      paletteGroups = new BufferedImage[paletteGroupNames.length];
      for(int i=0;i<paletteGroupNames.length;i++){
          try {
            NWNImage img = (NWNImage)mgr.getResource(paletteGroupNames[i]+".tga");
            paletteGroups[i] = img.getImage();
          } catch (Exception e){
              System.out.println("Failed to load palette = "+
                      paletteGroupNames[i]);
          }
      }
      // now set up open button
      jButton4.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          int selection = jTable1.getSelectedRow();
          openResource(mdl.getRowRD(selection));
        }
      });
      // now set up extract button
      jButton5.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          int selection = jTable1.getSelectedRow();
          extractResource(mdl.getRowRD(selection));
        }
      });

      // now set up the sort combo listener
      jComboBox1.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          sortResourcesBy(jComboBox1.getSelectedIndex());
        }
      });
      // Set up mod load
      jButton1.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          loadModule();
        }
      });

    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception {
    image1 = new ImageIcon(com.worldwizards.nwn.resbrowse.MainFrame.class.
                           getResource("openFile.png"));
    image2 = new ImageIcon(com.worldwizards.nwn.resbrowse.MainFrame.class.
                           getResource("closeFile.png"));
    image3 = new ImageIcon(com.worldwizards.nwn.resbrowse.MainFrame.class.
                           getResource("help.png"));
    contentPane = (JPanel)this.getContentPane();
    contentPane.setLayout(borderLayout1);
    this.setLocale(java.util.Locale.getDefault());
    this.setSize(new Dimension(400, 300));
    this.setTitle("NWN Resource Browser");
    statusBar.setText(" ");
    jMenuFile.setText("File");
    jMenuFileExit.setText("Exit");
    jMenuFileExit.addActionListener(new MainFrame_jMenuFileExit_ActionAdapter(this));
    jMenuHelp.setText("Help");
    jMenuHelpAbout.setText("About");
    jMenuHelpAbout.addActionListener(new MainFrame_jMenuHelpAbout_ActionAdapter(this));
    jButton1.setIcon(image1);
    jButton1.setToolTipText("Open File");
    jButton2.setIcon(image2);
    jButton2.setToolTipText("Close File");
    jButton3.setIcon(image3);
    jButton3.setToolTipText("Help");
    jTable1.setModel(tableModel1);
    jButton4.setText("OPEN RESOURCE");
    jToolBar.add(jButton1);
    jToolBar.add(jButton2);
    jToolBar.add(jButton3);
    jMenuFile.add(jMenuFileExit);
    jMenuHelp.add(jMenuHelpAbout);
    jMenuBar1.add(jMenuFile);
    jMenuBar1.add(jMenuHelp);
    this.setJMenuBar(jMenuBar1);
    contentPane.add(jToolBar, BorderLayout.NORTH);
    contentPane.add(jScrollPane1, BorderLayout.CENTER);
    contentPane.add(jPanel1, BorderLayout.SOUTH);
    jPanel1.setLayout(new GridLayout(4, 0));
    jPanel1.add(jButton4);
    jPanel1.add(jButton5);
    jPanel1.add(jComboBox1, null);
    jPanel1.add(statusBar);
    jScrollPane1.getViewport().add(jTable1, null);
  }

  public void openResource(ResourceDescriptor rd) {
    System.out.println("Open resource: " + rd);
    NWNResource resource = mgr.getResource(rd.getResRef(),
                                           (short) rd.getResType());
    if (resource.getClass() == NWNImage.class) {
      new ImageViewer(rd.getResRef() + "." + rd.getExt(), (NWNImage) resource);
    } else if (resource.getClass() == NWNPLT.class) {
      new PLTViewer(paletteGroups, new String[]
      {"Skin", "Hair", "Metal 1", "Metal 2", "Cloth 1", "Cloth 2",
       "Leather 1", "Leather 2","Tattoo 1","Tattoo 2"},rd.getResRef() + "." + rd.getExt(), (NWNPLT) resource);
    } else if (resource.getClass() == Win32INI.class) {
      new WinINIViewer(rd.getResRef() + "." + rd.getExt(), (Win32INI) resource);
    }
    else if (resource.getClass() == NWNText.class) {
      new TextViewer(rd.getResRef() + "." + rd.getExt(), (NWNText) resource);
    }
    else if (resource.getClass() == GFF.class) {
      switch (resource.getType()) {
        case 2012: // area
          //new AreaViewer(rd.getResRef() + "." + rd.getExt(), (GFF) resource,
          //               mgr);
          break;
      }
    } else if (resource.getClass() == RawBytes.class) {
      switch (resource.getType()) {
        case 2002: // mdl
          // new MDLViewer(rd.getResRef() + "." + rd.getExt(), (RawBytes) resource,
          //               mgr);
          break;
        case 2016: // WOK walk mesh  
          new WOKViewer(rd.getResRef() + "." + rd.getExt(), (NWNWOK) resource);
          break;	
      }

    }
  }

  public void extractResource(ResourceDescriptor rd) {
    System.out.println("Extract resource: " + rd);
    ByteBuffer resbuff = mgr.getRawResource(rd.getResRef(),
                                            (short) rd.getResType());
    File outf = new File(rd.getResRef() + "." + rd.getExt());
    try {
      FileOutputStream fos = new FileOutputStream(outf);
      FileChannel chan = fos.getChannel();
      chan.write(resbuff);
      chan.close();
      fos.close();
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }


  }

  //File | Exit action performed
  public void jMenuFileExit_actionPerformed(ActionEvent e) {
    System.exit(0);
  }

  //Help | About action performed
  public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
    MainFrame_AboutBox dlg = new MainFrame_AboutBox(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation( (frmSize.width - dlgSize.width) / 2 + loc.x,
                    (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.pack();
    dlg.show();
  }

  // Module load
  public void loadModule() {
    modFileChooser.setFileFilter(new FileFilter() {
      public boolean accept(File pathname) {
        return (pathname.isDirectory() ||
                (pathname.getName().endsWith(".mod")));
      }

      public String getDescription() {
        return "Module Files";
      }
    });
    int returnVal = modFileChooser.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = modFileChooser.getSelectedFile();
      try {
        mgr.addErf(new ERF(new FileInputStream(file).getChannel()));
        Set resourceSet = mgr.listResources();
        ResourceDescriptor[] rda = new ResourceDescriptor[resourceSet.size()];
        mdl =
            new BrowserTableModel( (ResourceDescriptor[]) resourceSet.toArray(
            rda));
        jTable1.setModel(mdl);
        updateStatus();
      }
      catch (FileNotFoundException ex) {
        ex.printStackTrace();
      }
    }

  }

  /**
   * updateStatus
   */
  private void updateStatus() {
    statusBar.setText("Total Resources catalogged: " +
                      mdl.getRowCount());
  }

  public void sortResourcesBy(int idx) {
    mdl.sortBy(idx);
  }

  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      jMenuFileExit_actionPerformed(null);
    }
  }
}

class MainFrame_jMenuFileExit_ActionAdapter
    implements ActionListener {
  MainFrame adaptee;

  MainFrame_jMenuFileExit_ActionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuFileExit_actionPerformed(e);
  }
}

class MainFrame_jMenuHelpAbout_ActionAdapter
    implements ActionListener {
  MainFrame adaptee;

  MainFrame_jMenuHelpAbout_ActionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuHelpAbout_actionPerformed(e);
  }
}
