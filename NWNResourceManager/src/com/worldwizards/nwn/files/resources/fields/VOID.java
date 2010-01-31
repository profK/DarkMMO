package com.worldwizards.nwn.files.resources.fields;

import com.worldwizards.nwn.files.resources.GFFField;

public class VOID
    extends GFFField {
  byte[] value;
  public VOID(String name, byte[] value) {
    super(name);
    this.value = value;
  }

  public void dump(String prefix) {
    super.dump(prefix);
    System.out.println(prefix + "Type: VOID");
    System.out.println(prefix + "Value: (byte aray of size " +value.length+
                       " )");
  }
}
