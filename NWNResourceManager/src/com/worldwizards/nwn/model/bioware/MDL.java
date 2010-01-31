package com.worldwizards.nwn.model.bioware;

import com.worldwizards.util.ByteBufferUtils;

import java.nio.*;
import java.util.*;


import java.io.Writer;
import java.io.IOException;
import java.io.InputStream;

public class MDL {
    ModelHeader model = null;

    List<AnimationHeader> animationHeaders = new ArrayList<AnimationHeader>();

    private static final boolean DEBUG = false;

    private byte geometryType;

    private static final int HDR_TYPE_OFFSET = 0x006C;

    public MDL(InputStream istream, boolean trace) {
        ByteBuffer buff = ByteBufferUtils.fromInputStream(istream);
        buff.order(ByteOrder.LITTLE_ENDIAN); // intel format file
        init(buff, trace);
    }

    public MDL(ByteBuffer buff, boolean trace) {
        init(buff, trace);
    }

    public void init(ByteBuffer mdlBuff, boolean trace) {
        // read MDL header
        mdlBuff.getInt(); // consume signiture
        int modelDataSize = mdlBuff.getInt();
        int rawDataSize = mdlBuff.getInt();
        if (modelDataSize<=0){
        	System.err.println("Got bad data size of "+modelDataSize);
        	return;
        }
        ByteOrder order = mdlBuff.order();
        ByteBuffer modelDataBuff = mdlBuff.slice();
        modelDataBuff.order(order);
        modelDataBuff.position(modelDataSize);
        ByteBuffer rawDataBuff = modelDataBuff.slice();
        rawDataBuff.order(order);
        modelDataBuff.position(0);
        // Now read and process headers
        model = new ModelHeader(modelDataBuff, rawDataBuff, trace);
        for (Iterator i = model.getAnimHeaderOffsets().iterator(); i.hasNext();) {
            modelDataBuff.position(((Integer) i.next()).intValue());
            animationHeaders.add(new AnimationHeader(modelDataBuff,
                    rawDataBuff, trace));
        }
    }

    /**
     * bytesToString
     *
     * @param superModelNameBuff byte[]
     * @return String
     */
    public static String bytesToString(byte[] bytes) {
        String s = new String(bytes);
        int nulloc = s.indexOf(0);
        if (nulloc > -1) {
            s = s.substring(0, nulloc);
        }
        return s;
    }

    /**
     * makeJ3DScene
     * <p/>
     * public SceneBase makeJ3DScene(TextureFactory textureFactory) {
     * SceneBase scene = new SceneBase();
     * scene.setSceneGroup(new BranchGroup());
     * model.addToJ3DScene(scene, textureFactory, scene.getSceneGroup());
     * return scene;
     * }
     */

    public void dump(Writer w) throws IOException {
        w.write("Dump of model: ");
        model.dump(w, "    ");
    }

    /**
     * @return
     */
    public MdlNode getRootModelNode() {

        return model.rootNode;
    }

    /**
     * @return
     */
    public List<AnimationHeader> listAnimationHeaders() {
        return animationHeaders;
	}
       
        
}
