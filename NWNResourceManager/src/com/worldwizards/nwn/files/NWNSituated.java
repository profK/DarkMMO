package com.worldwizards.nwn.files;

import com.worldwizards.nwn.files.resources.fields.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class NWNSituated {
  //note:  no D20 or game script specific stuff included
  byte animationState;
  int appearance;
  String description;
  String name;
  byte indestructable;
  String tag;

  /**
   * NWNPlacable
   *
   * @param struct Struct
   */
  public NWNSituated(Struct struct, int locale) {
    animationState = struct.getByte("AnimationState").byteValue();
    appearance = struct.getDWORD("Appearance").intValue();
    description = struct.getCExoLocString("Description").getString(locale);
    name = struct.getCExoLocString("LocName").getString(locale);
    indestructable = struct.getByte("Plot").byteValue();
    tag = struct.getCExoString("Tag").stringValue();
  }

  public int getAppearance(){
    return appearance;
  }

}
