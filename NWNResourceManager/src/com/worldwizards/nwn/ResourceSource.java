package com.worldwizards.nwn;

import com.worldwizards.nwn.files.NWNResource;
import java.util.Set;
import java.nio.ByteBuffer;

public interface ResourceSource {
  NWNResource getResource(String resRef, short resType);
  ByteBuffer getRawResource(String resRef, short resType);
  Set<ResourceDescriptor> listResources(Set<ResourceDescriptor> listToAddTo);
  Set<ResourceDescriptor> listResources();
}
