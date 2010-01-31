package com.worldwizards.nwn.files.resources.fields;

import com.worldwizards.nwn.files.resources.GFFField;

public class DWORD64 extends GFFField {
  long value;
  public DWORD64(String name, long value) {
  super(name);
  this.value = value;
}

public void dump(String prefix){
  super.dump(prefix);
  System.out.println(prefix+"Type: DWORD64");
  System.out.println(prefix+"Value: "+value);
}
}
