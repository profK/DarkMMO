package com.worldwizards.nwn.files;

import com.worldwizards.nwn.files.resources.GFF;
import com.worldwizards.nwn.files.resources.fields.Struct;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class NWNDoorTemplate extends NWNSituated{
  String comment;
  byte paletteID;
  String resRef;
  public NWNDoorTemplate(GFF resource) {
    this(resource.getRootStruct());
  }

  /**
   * NWNDoorTemplate
   *
   * @param struct Struct
   */
  public NWNDoorTemplate(Struct struct) {
    super(struct,0);
    comment = struct.getCExoString("Comment").stringValue();
    paletteID = struct.getByte("PaletteID").byteValue();
    resRef = struct.getResRef("TemplateResRef").stringValue();
  }

}
