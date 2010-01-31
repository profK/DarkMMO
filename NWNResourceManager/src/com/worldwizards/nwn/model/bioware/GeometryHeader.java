package com.worldwizards.nwn.model.bioware;

import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public abstract class GeometryHeader {
    private static final Logger logger = Logger.getLogger(GeometryHeader.class.getName());
    String name;
    MdlNode rootNode;
    private int nodeCount;
    private byte geometryType;
    private static final int HDR_SIZE = 0x0070;
    private static final int DUMMY_NODE = 0x0001;
    private static final int LIGHT_NODE = 0x0003;
    private static final int EMITTER_NODE = 0x0005;
    private static final int REFERENCE_NODE = 0x0011;
    private static final int TRIMESH_NODE = 0x0021;
    private static final int SKINMESH_NODE = 0x0061;
    private static final int ANIMMESH_NODE = 0x00A1;
    private static final int DANGLYMESH_NODE = 0x0121;
    private static final int AABBMESH_NODE = 0x0221;

    public GeometryHeader() {
    }

    /**
     * GeometryHeader
     *
     * @param modelDataBuff
     * @param rawDataBuff
     */
    public GeometryHeader(ByteBuffer modelDataBuff,
                          ByteBuffer rawDataBuff) {
        this(modelDataBuff, rawDataBuff, false);
    }

    /**
     * GeometryHeader
     *
     * @param modelDataBuff
     * @param rawDataBuff
     * @param trace
     */
    public GeometryHeader(ByteBuffer modelDataBuff,
                          ByteBuffer rawDataBuff, boolean trace) {
        //geometry header
        modelDataBuff.position(modelDataBuff.position() + 8); // skip first 2 4 bit values which are C function
        // pointers
        byte[] namebuff = new byte[64];
        modelDataBuff.get(namebuff);
        name = new String(namebuff);
        int nullloc = name.indexOf(0);
        if (nullloc > 0) {
            name = name.substring(0, nullloc);
        }
        int rootNodeOffset = modelDataBuff.getInt();
        int nodeCount = modelDataBuff.getInt();
        modelDataBuff.position(modelDataBuff.position() + 24); // skip 2 unkn pointers
        modelDataBuff.position(modelDataBuff.position() + 4); // skip ref count
        geometryType = modelDataBuff.get();
        modelDataBuff.position(modelDataBuff.position() + 3); // skip 3 bytes of padding
        loadSpecificHdrInfo(modelDataBuff, trace);
        rootNode = loadNode(rootNodeOffset, modelDataBuff, rawDataBuff, trace);
    }


    /**
     * loadNodes
     *
     * @param modelDataBuff ByteBuffer
     * @param rawDataBuff   ByteBuffer
     * @return Map
     */
    private MdlNode loadNode(int nodeOffset, ByteBuffer modelDataBuff,
                             ByteBuffer rawDataBuff, boolean trace) {
        MdlNode node = null;
        int nodeFlags = modelDataBuff.getInt(nodeOffset + 0x006C);
        switch (nodeFlags) {
            case DUMMY_NODE:
                node = new DummyNode(nodeOffset, modelDataBuff, rawDataBuff, trace);
                break;
            case LIGHT_NODE:
                node = new LightNode(nodeOffset, modelDataBuff, rawDataBuff, trace);
                break;
            case EMITTER_NODE:
                node = new EmitterNode(nodeOffset, modelDataBuff, rawDataBuff, trace);
                break;
            case REFERENCE_NODE:
                node = new ReferenceNode(nodeOffset, modelDataBuff, rawDataBuff, trace);
                break;
            case TRIMESH_NODE:
                node = new TrimeshNode(nodeOffset, modelDataBuff, rawDataBuff, trace);
                break;
            case SKINMESH_NODE:
                node = new SkinmeshNode(nodeOffset, modelDataBuff, rawDataBuff, trace);
                break;
            case ANIMMESH_NODE:
                node = new AnimmeshNode(nodeOffset, modelDataBuff, rawDataBuff, trace);
                break;
            case DANGLYMESH_NODE:
                node = new DanglymeshNode(nodeOffset, modelDataBuff, rawDataBuff, trace);
                break;
            case AABBMESH_NODE:
                node = new AABBmeshNode(nodeOffset, modelDataBuff, rawDataBuff, trace);
                break;
            default:
                logger.warning("Unknow node type " + nodeFlags +
                        ". Dummy substituted.");
                node = new DummyNode(nodeOffset, modelDataBuff, rawDataBuff, trace);
                break;
        }

        List childOffsets = node.getChildOffsets();
        for (Iterator i = childOffsets.iterator(); i.hasNext();) {
            MdlNode child = loadNode(((Integer) i.next()).intValue(), modelDataBuff,
                    rawDataBuff, trace);
            node.addChild(child);
        }
        return node;
    }

    public abstract void loadSpecificHdrInfo(ByteBuffer modelDataBuffer,
                                             boolean trace);

    /**
     * dump
     *
     * @param w      Writer
     * @param prefix String
     * @throws java.io.IOException
     */
    public void dump(Writer w, String prefix) throws IOException {
        w.write(prefix + "Header name = " + name + "\n");
        rootNode.dump(w, prefix);
    }

    public String getName() {
        return name;
    }

    public MdlNode getRootNode() {
        return rootNode;
    }

}
