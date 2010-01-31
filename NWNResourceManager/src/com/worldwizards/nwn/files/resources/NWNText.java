package com.worldwizards.nwn.files.resources;

import com.worldwizards.nwn.files.NWNResource;
import java.util.List;
import java.util.LinkedList;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import com.worldwizards.util.CharBufferReader;
import java.io.BufferedReader;
import java.util.Iterator;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.*;

public class NWNText
    extends NWNResource {

public static void register() {
   NWNResource.registerHandler(10,"txt",NWNText.class);
   NWNResource.registerHandler(2009,"nss",NWNText.class);
 }

 private static Charset charset = Charset.forName("ISO-8859-15");
 private static CharsetDecoder decoder = charset.newDecoder();


 List textLines = new LinkedList();

  public NWNText(String name,short type,ByteBuffer buff) {
   super(name,type);
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
   try {
     while (brdr.ready()) {
       textLines.add(brdr.readLine());
     }
   } catch (Exception e) {
     e.printStackTrace();
   }
 }

 public void dump() {
   super.dump();
   for(Iterator i=textLines.iterator();i.hasNext();){
     System.out.println((String)i.next());
   }
 }

 public String getTypeName(){
   return "text";
 }

 public List getTextLines(){
   return textLines;
 }

}
