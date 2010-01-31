package com.worldwizards.nwn.files;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

import com.worldwizards.nwn.*;

class ERFEntry {
  String resRef;
  short resType;
  int resID;
  int offset;
  int size;
  /**
   * ERFEntry
   *
   * @param string String
   * @param s short
   * @param i int
   * @param i1 int
   * @param i2 int
   */
  public ERFEntry(String resRef, short resType, int resID,
                  int offset, int size) {
    this.resRef = resRef;
    this.resType = resType;
    this.resID = resID;
    this.offset = offset;
    this.size = size;
  }

}

public class ERF
    implements ResourceSource {
  FileChannel chan;
  ByteOrder order;
  String fileType;
  String version;
  int entryCount;
  short buildYear;
  short buildDay;
  short descriptionStrRef;
  StringList localizedStringList;
  //NWNResource[] resourceArray;
  Map resourceByName = new HashMap();
  private static final boolean DEBUG = false;
  private static final long ERF_HDR_SIZE = 64;
  private static final int KEY_ENTRY_SIZE = 32;
  private static final int RESOURCE_ENTRY_SIZE = 16;

  public ERF(FileChannel chan) {
    this(chan, ByteOrder.LITTLE_ENDIAN);
  }

  public ERF(FileChannel chan, ByteOrder order) {
    this.chan = chan;
    this.order = order;
    ByteBuffer buff = null;

    try {
      buff = chan.map(FileChannel.MapMode.READ_ONLY, 0, ERF_HDR_SIZE);
      buff.order(order);
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
    
    byte[] inbytes = new byte[4];
    buff.get(inbytes);
    fileType = new String(inbytes);
    buff.get(inbytes);
    version = new String(inbytes);
    // header stuff for building tables
    int languageCount = buff.getInt();
    int localizedStringSize = buff.getInt();
    int entryCount = buff.getInt();
    int offsetToLocalizedString = buff.getInt();
    int offsetToKeyList = buff.getInt();
    int offsetToResourceList = buff.getInt();
    buildYear = buff.getShort();
    buildDay = buff.getShort();
    descriptionStrRef = buff.getShort();
    //  Header load complete, now parse file and build tables
    // Localized String Module info
    try {
      buff = chan.map(FileChannel.MapMode.READ_ONLY, offsetToLocalizedString,
                      localizedStringSize);
      buff.order(order);
    }
    catch (IOException ex1) {
      ex1.printStackTrace();
      return;
    }
    localizedStringList = new StringList(buff, languageCount);
    if (DEBUG) {
      System.out.println("Localized String Table:");
      localizedStringList.dump(System.out);
    }
    // ERF resources
    // create a mapped buff for the key list
    try {
      buff = chan.map(FileChannel.MapMode.READ_ONLY, offsetToKeyList,
                      entryCount * KEY_ENTRY_SIZE);
      buff.order(order);
    }
    catch (IOException ex1) {
      ex1.printStackTrace();
      return;
    }
    // create a mapped buff for the resourceListEntries
    ByteBuffer resourceListBuff;
    try {
      resourceListBuff = chan.map(FileChannel.MapMode.READ_ONLY,
                                  offsetToResourceList,
                                  entryCount * RESOURCE_ENTRY_SIZE);
      resourceListBuff.order(order);
    }
    catch (IOException ex1) {
      ex1.printStackTrace();
      return;
    }

    for (int i = 0; i < entryCount; i++) {
      // get key
      inbytes = new byte[16];
      buff.get(inbytes);
      int firstNull = 0;
      for (; firstNull < inbytes.length; firstNull++) {
        if (inbytes[firstNull] == 0) {
          break;
        }
      }
      String resRef = new String(inbytes, 0, firstNull);
      int resID = buff.getInt();
      if (resID != i) {
        System.err.println("Warning: ERF Key #" + resID + " found in position " +
                           i);
      }
      short resType = buff.getShort();
      buff.getShort(); // advance past padding
      // Get resource position and size
      int offsetToResource = resourceListBuff.getInt();
      int resourceSize = resourceListBuff.getInt();
      resourceByName.put(resRef + "." + resType,
                         new ERFEntry(resRef, resType, resID, offsetToResource,
                                      resourceSize));
    }
    if (DEBUG) {
      dumpResources();
    }
  }

  /**
   * dumpResources
   */
  private void dumpResources() {

  }

  //  test main
  static public void main(String[] args) {
    for (int i = 0; i < args.length; i++) {
      FileInputStream fis = null;
      try {
        fis = new FileInputStream(args[i]);
      }
      catch (FileNotFoundException ex) {
        ex.printStackTrace();
      }
      FileChannel fchan = fis.getChannel();
      try {
        ERF erf = new ERF(fchan);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
  
  public NWNResource getResource(String resRef,String ext){
      return getResource(resRef,ResourceID.idForExt(ext));
  }

   
  /**
   * getResource
   *
   * @param resRef String
   * @param resType short
   * @return NWNResource
   */
  public NWNResource getResource(String resRef, short resType) {
    ByteBuffer resourceBuff = getRawResource(resRef, resType);
    if (resourceBuff == null) {
      return null;
    }
    return NWNResource.newResource(resourceBuff, resRef, resType);
  }

    /**
   * getResourceDescriptor
   *
   * @param resRef String
   * @param resType short
   * @return NWNResource
   */
  public ResourceDescriptor getResourceDescriptor(String resRef, short resType) {
    ERFEntry entry = (ERFEntry) resourceByName.get(resRef + "." + resType);
    if (entry == null) {
        return null;
    } else {
        return new ResourceDescriptor(entry.resRef, entry.resType);
    }
  }

  /**
   * getRawResource
   *
   * @param resRef String
   * @param resType short
   * @return NWNResource
   */
  public ByteBuffer getRawResource(String resRef, short resType) {
    ByteBuffer resourceBuff;
    ERFEntry entry = (ERFEntry) resourceByName.get(resRef + "." + resType);
    if (entry == null) {
      return null;
    }
    try {
      resourceBuff = chan.map(FileChannel.MapMode.READ_ONLY,
                              entry.offset, entry.size);
      resourceBuff.order(order);
    }
    catch (IOException ex1) {
      ex1.printStackTrace();
      return null;
    }
    return resourceBuff;
  }
  
  /**
   * 
   * @param resRef
   * @param resType
   * @return
   */
  
  public long getRawResourceSize(String resRef, short resType) {
    ERFEntry entry = (ERFEntry) resourceByName.get(resRef + "." + resType);
    if (entry == null) {
      return 0;
    }
    return entry.size;
  }

  /**
   * listResources
   *
   * @return Set
   */
  public Set<ResourceDescriptor> listResources() {
    return listResources(new HashSet<ResourceDescriptor>());
  }

  /**
   * listResources
   *
   * @param listToAddTo Set
   * @return Set
   */
  public Set<ResourceDescriptor> listResources(
          Set<ResourceDescriptor> listToAddTo) {
    for (Iterator i = resourceByName.values().iterator(); i.hasNext(); ) {
      ERFEntry entry = (ERFEntry) i.next();
      listToAddTo.add(new ResourceDescriptor(entry.resRef, entry.resType));
    }
    return listToAddTo;

  }

  /**
   * listResources
   *
   * @param resourceSet Set
   * @param pattern String
   */
  public Set listResources(Set listToAddTo, int resType) {
    for (Iterator i = resourceByName.values().iterator(); i.hasNext(); ) {
      ERFEntry entry = (ERFEntry) i.next();
      if (entry.resType == resType){
        listToAddTo.add(new ResourceDescriptor(entry.resRef, entry.resType));
      }
    }
    return listToAddTo;

  }

}
