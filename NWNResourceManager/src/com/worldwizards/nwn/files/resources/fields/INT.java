package com.worldwizards.nwn.files.resources.fields;

import com.worldwizards.nwn.files.resources.GFFField;

public class INT
    extends GFFField {
  int value;
  public INT(String name, int value) {
    super(name);
    this.value = value;
  }

  public void dump(String prefix) {
    super.dump(prefix);
    System.out.println(prefix + "Type: INT");
    System.out.println(prefix + "Value: " + value);
  }

  public int intValue(){
    return value;
  }
}
