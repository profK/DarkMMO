package com.worldwizards.nwn.files;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.FileChannel.*;
import java.util.*;

import com.worldwizards.nwn.*;

public class ResourceDirectory
    implements ResourceSource {

  File dir;
  /**
   * ResourceDirectory
   *
   * @param file File
   */
  public ResourceDirectory(File file) throws InstantiationException {
    if (!file.isDirectory()) {
      throw new InstantiationException("Error " + file.toString() +
                                       " is not a directory.");
    }
    dir = file;
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
   * getResource
   *
   * @param resRef String
   * @param resType short
   * @return NWNResource
   */
  public ByteBuffer getRawResource(String resRef, short resType) {
    String filename = resRef + "." + NWNResource.getExtName(resType);
    File resfile = new File(dir, filename);
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(resfile);
      FileChannel chan = fis.getChannel();
      ByteBuffer buff = chan.map(MapMode.READ_ONLY, 0, resfile.length());
      return buff;
    }
    catch (IOException ex1) {
      ex1.printStackTrace();
    }
    return null;
  }

public long getRawResourceSize(String resRef, short resType) {
        String filename = resRef + "." + NWNResource.getExtName(resType);
        File resfile = new File(dir, filename);
        if (resfile.exists()){
            return resfile.length();
        }
        return 0;
    }

  /**
   * listResources
   *
   * @param listToAddTo Set
   * @return Set
   */
  public Set listResources(Set listToAddTo) {
    File[] resourceFiles = dir.listFiles();
    for (int i = 0; i < resourceFiles.length; i++) {
      String resRef = resourceFiles[i].getName();
      int dotloc = resRef.indexOf('.');
      if (dotloc != -1) {
        String ext = resRef.substring(dotloc + 1);
        resRef = resRef.substring(0, dotloc);
        int resType = NWNResource.getResType(ext);
        if (resType != -1) {
          listToAddTo.add(new ResourceDescriptor(resRef, resType));
        }
      }
    }
    return listToAddTo;
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
      File dir = new File(args[i]);
      ResourceDirectory rd = null;
      try {
        rd = new ResourceDirectory(dir);
      }
      catch (InstantiationException ex1) {
        ex1.printStackTrace();
      }
      Set resourceSet = rd.listResources();
      for (Iterator it = resourceSet.iterator(); it.hasNext(); ) {
        System.out.println( ( (ResourceDescriptor) it.next()).toString());
      }
    }
  }

  /**
   * listResources
   *
   * @param resourceSet Set
   * @param resType int
   */
  public Set listResources(Set listToAddTo, int resType) {
    File[] resourceFiles = dir.listFiles();
   for (int i = 0; i < resourceFiles.length; i++) {
     String resRef = resourceFiles[i].getName();
     int dotloc = resRef.indexOf('.');
     if (dotloc != -1) {
       String ext = resRef.substring(dotloc + 1);
       resRef = resRef.substring(0, dotloc);
       int fileResType = NWNResource.getResType(ext);
       if (fileResType == resType) {
         listToAddTo.add(new ResourceDescriptor(resRef, resType));
       }
     }
   }
   return listToAddTo;

  }

}
