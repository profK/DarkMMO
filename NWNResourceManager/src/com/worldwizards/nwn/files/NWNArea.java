package com.worldwizards.nwn.files;

import com.worldwizards.nwn.files.resources.*;
import com.worldwizards.nwn.files.resources.fields.*;
import java.io.Serializable;

class TileStruct implements Serializable {

    public final static long serialVersionUID = 1L;
    int tilenum;
    int orientation;
    int elevation;
    int lightColorIdx1;
    int lightColorIdx2;
    byte sourceLight1;
    byte sourceLight2;
    boolean anim1;
    boolean anim2;
    boolean anim3;

    /**
     * TileStruct
     *
    
     */
    public TileStruct(int tilenum, int orientation, int elevation,
            int lightColorIdx1, int lightColorIdx2,
            byte sourceLight1, byte sourceLight2,
            boolean anim1, boolean anim2, boolean anim3) {
        this.tilenum = tilenum;
        this.elevation = elevation;
        this.orientation = orientation;
        this.lightColorIdx1 = lightColorIdx1;
        this.lightColorIdx2 = lightColorIdx2;
        this.sourceLight1 = sourceLight1;
        this.sourceLight2 = sourceLight2;
        this.anim1 = anim1;
        this.anim2 = anim2;
        this.anim3 = anim3;
    }
}

public class NWNArea implements Serializable {

    public final static long serialVersionUID = 1L;
    GFF gff;
    String name;
    int width;
    int height;
    TileStruct[][] tileMap;
    String tileSetResRef;

    public NWNArea(GFF resource) {
        gff = resource;
        name = gff.getCExoLocString("Name").getString(0);
        width = gff.getInt("Width").intValue();
        height = gff.getInt("Height").intValue();
        tileSetResRef = gff.getResRef("Tileset").stringValue();
        tileMap = new TileStruct[width][height];
        // now get tile descriptions
        GFFList tileList = gff.getList("Tile_List");
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                Struct gffTileStruct = tileList.getStruct(i + (j * width));
                boolean anim1 = (gffTileStruct.getByte("Tile_AnimLoop1").byteValue() == 1);
                boolean anim2 = (gffTileStruct.getByte("Tile_AnimLoop2").byteValue() == 1);
                boolean anim3 = (gffTileStruct.getByte("Tile_AnimLoop3").byteValue() == 1);
                int elevation = gffTileStruct.getInt("Tile_Height").intValue();
                int tilenum = gffTileStruct.getInt("Tile_ID").intValue();
                byte lightColorIdx1 = gffTileStruct.getByte("Tile_MainLight1").byteValue();
                byte lightColorIdx2 = gffTileStruct.getByte("Tile_MainLight1").byteValue();
                int orientation = gffTileStruct.getInt("Tile_Orientation").intValue();
                byte sourceLight1 = gffTileStruct.getByte("Tile_SrcLight1").byteValue();
                byte sourceLight2 = gffTileStruct.getByte("Tile_SrcLight2").byteValue();
                tileMap[i][j] = new TileStruct(tilenum, orientation, elevation,
                        lightColorIdx1, lightColorIdx2,
                        sourceLight1, sourceLight2,
                        anim1, anim2, anim3);
            }
        }
    }

    /**
     * getWidth
     *
     * @return int
     */
    public int getWidth() {
        return width;
    }

    /**
     * getHeight
     *
     * @return int
     */
    public int getHeight() {
        return height;
    }

    /**
     * getTileIdx
     *
     * @param i int
     * @param j int
     * @return int
     */
    public int getTileNum(int i, int j) {
        return tileMap[i][j].tilenum;
    }

    /**
     * getTileOrientation
     *
     * @param i int
     * @param j int
     * @return int
     */
    public int getTileOrientation(int i, int j) {
        return tileMap[i][j].orientation;
    }

    public int getTileElevation(int i, int j) {
        return tileMap[i][j].elevation;
    }

    /**
     * getTileSetResRef
     *
     * @return Object
     */
    public String getTileSetResRef() {
        return tileSetResRef;
    }

    public String getName() {
        return name;
    }
}
