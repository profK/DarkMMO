package com.worldwizards.nwn.files;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.worldwizards.nwn.NoDialogTalkFileException;
import com.worldwizards.nwn.ResourceManager;
import com.worldwizards.nwn.files.resources.GFF;
import com.worldwizards.nwn.files.resources.NWNWOK;
import com.worldwizards.nwn.files.resources.fields.GFFList;
import com.worldwizards.nwn.files.resources.fields.Struct;

public class NWNTilesetPalette {

    public enum NODETYPE {

        FEATURES, GROUPS, TERRAIN
    };

    public static void register() {
        NWNResource.registerHandler(2030, "itp", NWNTilesetPalette.class);
    }

    public class TileGroup {

        public String name;
        public NODETYPE nodeType;
        public List<TileNode> tileNodes = new ArrayList<TileNode>();

        public TileGroup(String name, NODETYPE nodeType) {
            this.name = name;
            this.nodeType = nodeType;
        }

        public void addTileNode(TileNode tn) {
            tileNodes.add(tn);
        }
    }

    public class TileNode {

        public String name;
        public String resRef;

        public TileNode(String name, String resRef) {
            this.name = name;
            this.resRef = resRef;
        }
    }
    List<TileGroup> groups = new ArrayList<TileGroup>();

    public NWNTilesetPalette(String name, short type, ByteBuffer buff) {
        throw new UnsupportedOperationException("this construcrtor not yet supported");
    }

    public NWNTilesetPalette(ResourceManager mgr, GFF resource) throws NoDialogTalkFileException {
        this(mgr, resource.getRootStruct(), 0);
    }

    public NWNTilesetPalette(ResourceManager mgr, Struct gffRootStruct, int locale) throws NoDialogTalkFileException {
        GFFList rootList = gffRootStruct.getList("MAIN");
        for (int i = 0; i < rootList.length(); i++) {
            Struct groupStruct = rootList.getStruct(i);
            TileGroup group =
                    new TileGroup(mgr.getDialogString(groupStruct.getDWORD("STRREF").intValue()),
                    NODETYPE.values()[groupStruct.getByte("ID").byteValue()]);
            GFFList tnodeList = groupStruct.getList("LIST");

            groups.add(group);
            if (tnodeList != null) {
                for (int idx = 0; idx < tnodeList.length(); idx++) {
                    Struct tileNodeStruct = tnodeList.getStruct(idx);
                    String name;
                    if (tileNodeStruct.hasField("NAME")) {
                        name = tileNodeStruct.getCExoString("NAME").stringValue();
                    } else {
                        name = mgr.getDialogString(tileNodeStruct.getDWORD("STRREF").intValue());
                    }
                    group.addTileNode(new TileNode(name, tileNodeStruct.getResRef("RESREF").stringValue()));
                }
            }
        }
    }

    public List<TileGroup> listGroups() {
        return groups;
    }
}
