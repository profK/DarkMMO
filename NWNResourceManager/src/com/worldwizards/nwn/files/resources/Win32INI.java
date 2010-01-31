package com.worldwizards.nwn.files.resources;

import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;
import java.util.Map.*;

import com.worldwizards.nwn.files.*;
import com.worldwizards.util.*;

public class Win32INI
    extends NWNResource {
  public static void register() {
    NWNResource.registerHandler(7, "ini", Win32INI.class);
    NWNResource.registerHandler(2013, "set", Win32INI.class);
    NWNResource.registerHandler(2045, "dft", Win32INI.class);
  }

  private static Charset charset = Charset.forName("ISO-8859-15");
  private static CharsetDecoder decoder = charset.newDecoder();
  private static final boolean DEBUG = false;
  Map iniParagraphs = new HashMap();

  public Win32INI(String name, short type, ByteBuffer buff) {
    super(name, type);
    CharBuffer cbuffer = null;
    try {
      cbuffer = decoder.decode(buff);
    }
    catch (CharacterCodingException ex) {
      ex.printStackTrace();
      return;
    }
    CharBufferReader rdr = new CharBufferReader(cbuffer);
    BufferedReader brdr = new BufferedReader(rdr);
    Map currentParagraph = null;
    try {
      while (brdr.ready()) {
        String textline = brdr.readLine();
        textline = textline.trim();
        if (textline.length() == 0 ) {
          // empty line
          continue;
        } else if (textline.charAt(0) == ';'){
          // comment
          continue;
        } else if (textline.charAt(0) == '[') { //new paragraph
          textline = textline.substring(1, textline.length() - 1).trim();
          currentParagraph = new HashMap();
          if (DEBUG){
            System.out.println("Found paragraph: "+textline);
          }
          iniParagraphs.put(textline, currentParagraph);
        }
        else {
          int equalpos = textline.indexOf('=');
          String key = textline;
          String value = "TRUE";
          if (equalpos != -1) {
            key = textline.substring(0, equalpos).trim();
            if (equalpos != textline.length()){
              value =
                  textline.substring(equalpos + 1, textline.length()).trim();
            }
          }
          currentParagraph.put(key, value);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void dump() {
    super.dump();
    for (Iterator i = iniParagraphs.entrySet().iterator(); i.hasNext(); ) {
      Entry e = (Entry) i.next();
      System.out.println("[" + (String) e.getKey() + "]");
      for (Iterator i2 = ( (HashMap) e.getValue()).entrySet().iterator();
           i2.hasNext(); ) {
        Entry e2 = (Entry) i2.next();
        System.out.println( (String) e2.getKey() + "=" + (String) e2.getValue());
      }
    }
  }

  public String getTypeName() {
    return "win32 ini";
  }

  public Iterator getParagraphIterator(){
    return iniParagraphs.entrySet().iterator();
  }

  /**
   * getINIEntry
   *
   * @param paragraph String
   * @param entry String
   * @return String
   */
  public String getINIEntry(String paragraph, String entry) {
    Map pmap = (Map)iniParagraphs.get(paragraph);
    return (String)pmap.get(entry);
  }

}
