package com.worldwizards.nwn.files;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.util.Map.*;

import com.worldwizards.nwn.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */



class KeyEntry {
  String resName;
  short resType;
  int fileIdx;
  int entryIdx;
  /**
   * ResourceEntry
   *
   * @param string String
   * @param s short
   * @param i int
   * @param i1 int
   */
  public KeyEntry(String resName, short resType, int fileIdx,
                  int entryIdx) {
    this.resName = resName;
    this.resType = resType;
    this.fileIdx = fileIdx;
    this.entryIdx = entryIdx;
  }

  /**
   * getResType
   *
   * @return Object
   */
  public int getResType() {
    return resType;
  }

  /**
   * getResRef
   *
   * @return String
   */
  public String getResRef() {
    return resName;
  }

    long getSize() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}

public class KeyTable
    implements ResourceSource {
  
  
  FileChannel chan;
  ByteOrder order;
  private static final long KEYTBL_HDR_SIZE = 32;
  private static final int FILE_ENTRY_SIZE = 12;
  private static final int KEY_ENTRY_SIZE = 22;
  BIFFile[] fileEntries;
  Map resources = new HashMap();
  File dir;
  
  public KeyTable(File keyFile){
        try {
            dir = keyFile.getParentFile();
            chan = new java.io.FileInputStream(keyFile).getChannel();
            this.order = java.nio.ByteOrder.LITTLE_ENDIAN;
            loadTable(chan);
        } catch (FileNotFoundException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
  }
  
  public KeyTable(FileChannel chan) {
    this(chan, ByteOrder.LITTLE_ENDIAN);
  }

  public KeyTable(FileChannel chan, ByteOrder order) {
    this.chan = chan;
    this.order = order;
    loadTable(chan);
  }

    
    
 private void loadTable(FileChannel chan){
    int buildYear;
    int buildDay;
    ByteBuffer buff;
    List fileTable = new ArrayList();
    try {
      buff = chan.map(FileChannel.MapMode.READ_ONLY, 0, KEYTBL_HDR_SIZE);
      buff.order(order);
    }
    catch (IOException ex) {
      ex.printStackTrace();
      return;
    }
    byte[] fourbb = new byte[4];
    buff.get(fourbb);
    if (!new String(fourbb).equals("KEY ")) {
      System.err.println("Error: File is not a KEY file.");
      return;
    }
    buff.get(fourbb);
    if (!new String(fourbb).equals("V1  ")) {
      System.err.println("Error: KEY file is not version V1");
      return;
    }
    int biffCount = buff.getInt();
    int keyCount = buff.getInt();
    int offsetToFileTable = buff.getInt();
    int offsetToKeyTable = buff.getInt();
    buildYear = buff.getInt();
    buildDay = buff.getInt();
    // now set up buffers
    ByteBuffer fileBuff;
    ByteBuffer fileNameBuff;
    ByteBuffer keyBuff;
    String dirPath = ""; // local to working dir
    if (dir !=null){
            try {
                dirPath = dir.getCanonicalPath() + java.io.File.separator;
            } catch (IOException ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
                return;
            }
    }
    try {
      fileBuff = chan.map(FileChannel.MapMode.READ_ONLY, offsetToFileTable,
                          biffCount * FILE_ENTRY_SIZE);
      fileBuff.order(order);
      //int offsetToFileNameTable = offsetToFileTable+(biffCount*FILE_ENTRY_SIZE);
      int offsetToFileNameTable = 0;
      fileNameBuff = chan.map(FileChannel.MapMode.READ_ONLY,
                              offsetToFileNameTable,
                              offsetToKeyTable - offsetToFileNameTable);
      fileNameBuff.order(order);
      keyBuff = chan.map(FileChannel.MapMode.READ_ONLY, offsetToKeyTable,
                         keyCount * KEY_ENTRY_SIZE);
      keyBuff.order(order);
    }
    catch (IOException ex) {
      ex.printStackTrace();
      return;
    }
    // now load file Table
    fileEntries = new BIFFile[biffCount];
    for (int i = 0; i < biffCount; i++) {
      int size = fileBuff.getInt();
      int nameOffset = fileBuff.getInt();
      short nameSize = fileBuff.getShort();
      short driveMask = fileBuff.getShort();
      byte[] namebuff = new byte[nameSize - 1];
      fileNameBuff.position(nameOffset);
      fileNameBuff.get(namebuff);
      fileEntries[i] = new BIFFile(dirPath+(new String(namebuff)), driveMask, order);
    }
    // now load reosurce keys
    byte[] resNameBytes = new byte[16];
    for (int i = 0; i < keyCount; i++) {
      keyBuff.get(resNameBytes);
      String resRef = new String(resNameBytes);
      int firstNull = resRef.indexOf(0);
      if (firstNull != -1) {
        resRef = resRef.substring(0, firstNull);
      }
      short resType = keyBuff.getShort();
      int resID = keyBuff.getInt();
      resources.put(resRef + "." + resType,
                    new KeyEntry(resRef, resType, (resID >> 20) & 0xFFF,
                                      resID & 0xFFFFF));

    }

  }

  public void dump() {
    for (int i = 0; i < fileEntries.length; i++) {
      System.out.println("File: " + fileEntries[i].getName());
    }
    for(Iterator i =resources.entrySet().iterator();i.hasNext();){
      Entry entry = (Entry)i.next();
      System.out.print("Resource: "+(String)entry.getKey());
      String ext =
          NWNResource.getExtByID(((KeyEntry)entry.getValue()).getResType());
    }

  }


  /**
   * getResource
   *
   * @param resRef String
   * @param resType short
   * @return NWNResource
   */
  public ByteBuffer getRawResource(String resRef, short resType) {
    ByteBuffer resourceBuff;
    KeyEntry entry = (KeyEntry) resources.get(resRef + "." + resType);
    if (entry == null) {
      return null;
    }

    int fileIdx = entry.fileIdx;
    int resIdx = entry.entryIdx;
    BIFFile bif = fileEntries[fileIdx];
    return bif.getRawResource(resRef,resIdx);
  }
  
  public long getRawResourceSize(String resRef, short resType) {
    KeyEntry entry = (KeyEntry) resources.get(resRef + "." + resType);
    if (entry == null) {
      return 0;
    }

    int fileIdx = entry.fileIdx;
    int resIdx = entry.entryIdx;
    BIFFile bif = fileEntries[fileIdx];
    return bif.getRawResourceSize(resRef,resIdx);
  }

  /**
     * getResource
     *
     * @param resRef String
     * @param resType short
     * @return NWNResource
     */
    public NWNResource getResource(String resRef, short resType) {
      ByteBuffer resourceBuff = getRawResource(resRef,resType);
      if (resourceBuff == null) {
        return null;
      }
      return NWNResource.newResource(resourceBuff, resRef, resType);
    }



  /**
   * listResources
   *
   * @param listToAddTo Set
   * @return Set
   */
  public Set listResources(Set setToAddTo) {
    for(Iterator i = resources.entrySet().iterator();i.hasNext();){
      Entry entry = (Entry)i.next();
      KeyEntry ke = (KeyEntry)entry.getValue();
      setToAddTo.add(new ResourceDescriptor(ke.getResRef(),ke.getResType()));
    }
    return setToAddTo;
  }

  /**
   * listResources
   *
   * @return Set
   */
  public Set listResources() {
    return listResources(new HashSet());
  }

  // unit test
 public static void main(String[] args) {
   for (int i = 0; i < args.length; i++) {
     try {
       FileInputStream fis = new FileInputStream(
           new File(args[i]));
       KeyTable kt = new KeyTable(fis.getChannel());
       Set resourceSet = kt.listResources();
       for(Iterator it= resourceSet.iterator();it.hasNext();){
         System.out.println(((ResourceDescriptor)it.next()).toString());
       }
     }
     catch (FileNotFoundException ex) {
       ex.printStackTrace();
     }
   }
 }

  /**
   * listResources
   *
   * @param resourceSet Set
   * @param resType int
   */
  public Set listResources(Set setToAddTo, int resType) {
    for(Iterator i = resources.entrySet().iterator();i.hasNext();){
      Entry entry = (Entry)i.next();
      KeyEntry ke = (KeyEntry)entry.getValue();
      if (ke.getResType() == resType) {
        setToAddTo.add(new ResourceDescriptor(ke.getResRef(), ke.getResType()));
      }
    }
    return setToAddTo;
  }

}
