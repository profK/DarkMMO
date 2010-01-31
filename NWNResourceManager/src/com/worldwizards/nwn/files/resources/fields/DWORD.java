package com.worldwizards.nwn.files.resources.fields;

import com.worldwizards.nwn.files.resources.*;

public class DWORD
    extends GFFField {
  int value;
  public DWORD(String name, int value) {
    super(name);
    this.value = value;
  }

  public void dump(String prefix) {
    super.dump(prefix);
    System.out.println(prefix + "Type: DWORD");
    System.out.println(prefix + "Value: " + value);
  }

  /**
   * intValue
   *
   * @return int
   */
  public int intValue() {
    return value;
  }
}
