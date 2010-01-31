package com.worldwizards.nwn.files.resources.fields;

import com.worldwizards.nwn.files.resources.GFFField;

public class SHORT
    extends GFFField {
  short value;
  public SHORT(String name, short value) {
    super(name);
    this.value = value;
  }

  public void dump(String prefix) {
    super.dump(prefix);
    System.out.println(prefix + "Type: SHORT");
    System.out.println(prefix + "Value: " + value);
  }
}
