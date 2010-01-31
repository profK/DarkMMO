package com.worldwizards.nwn.files.resources;

import com.worldwizards.nwn.files.NWNResource;
import java.nio.ByteBuffer;


public class RawBytes  extends NWNResource {
  public static void register() {
   NWNResource.registerHandler(2002,"mdl",RawBytes.class);
 }

 ByteBuffer buff;


 public RawBytes(String name,short type,ByteBuffer buff) {
   super(name,type);
   this.buff = buff;
  }

  public ByteBuffer getByteBuffer() {
    return buff;
  }
}
