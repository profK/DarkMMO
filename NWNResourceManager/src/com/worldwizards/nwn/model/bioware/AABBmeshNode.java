package com.worldwizards.nwn.model.bioware;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


import com.worldwizards.nwn.model.bioware.AABBTreeNode.PLANE;


/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class AABBmeshNode extends TrimeshNode {

	private AABBTreeNode root = null;

	public int fileDataSize() {
		return 0x0274;
	}

	/**
	 * AABBmeshNode
	 * 
	 * @param pos
	 *            int
	 * @param modelDataBuff
	 *            ByteBuffer
	 * @param rawDataBuff
	 *            ByteBuffer
	 */
	public AABBmeshNode(int pos, ByteBuffer modelDataBuff,
			ByteBuffer rawDataBuff, boolean trace) {
		super(pos, modelDataBuff, rawDataBuff, trace);
		if (trace ) {
			System.out.println("AABB Mesh Node: " + name);
		}
		modelDataBuff.position(pos + 0x0070 + 0x00270); // skip node header aand
														// mesh hdr
		int AABBentryPointer = modelDataBuff.getInt();
		modelDataBuff.position(AABBentryPointer);
		root = readNode(modelDataBuff);

	}

	/**
	 * @param modelDataBuff
	 * @return
	 */
	private AABBTreeNode readNode(ByteBuffer modelDataBuff) {
		float[] vals = new float[6];
		vals[0] = modelDataBuff.getFloat();
		vals[1] = modelDataBuff.getFloat();
		vals[2] = modelDataBuff.getFloat();
		vals[3] = modelDataBuff.getFloat();
		vals[4] = modelDataBuff.getFloat();
		vals[5] = modelDataBuff.getFloat();
		int leftPos = modelDataBuff.getInt();
		int rightPos = modelDataBuff.getInt();
		int leafID = modelDataBuff.getInt();
		List<PLANE> planesList = new ArrayList<PLANE>();
		int planesFlags = modelDataBuff.getInt();
		if ((planesFlags & 0x01) == 0x01) {
			planesList.add(PLANE.PositiveX);
		}
		if ((planesFlags & 0x02) == 0x02) {
			planesList.add(PLANE.PositiveY);
		}
		if ((planesFlags & 0x04) == 0x04) {
			planesList.add(PLANE.PositiveZ);
		}
		if ((planesFlags & 0x08) == 0x08) {
			planesList.add(PLANE.NegativeX);
		}
		if ((planesFlags & 0x10) == 0x10) {
			planesList.add(PLANE.NegativeY);
		}
		if ((planesFlags & 0x20) == 0x20) {
			planesList.add(PLANE.NegativeZ);
		}
		PLANE[] pa = new PLANE[planesList.size()];
		AABBTreeNode node = new AABBTreeNode(vals[0], vals[1], vals[2],
				vals[3], vals[4], vals[5], planesList.toArray(pa));
		if (leafID != -1) {
			node.setLeafFace(leafID);
		} else {
            if (leftPos>=0){
			    modelDataBuff.position(leftPos);
			    node.setLeftNode(readNode(modelDataBuff));
            } else {
                node.setLeftNode(null);
            }
            if (rightPos>=0){
			    modelDataBuff.position(rightPos);
			    node.setRightNode(readNode(modelDataBuff));
            } else {
                node.setRightNode(null);
            }
		}
		return node;
	}

	/**
	 * addNodeToJ3DGraph
	 * 
	 * @param textureFactory
	 *            TextureFactory
	 * @param branchGroup
	 *            BranchGroup
	 * @param positionNode
	 *            TransformGroup
	 * @param orientationNode
	 *            TransformGroup
	 *
	public void addNodeToJ3DGraph(TextureFactory textureFactory,
			Group branchGroup, TransformGroup positionNode,
			TransformGroup orientationNode) {
	}
         * /

	/**
	 * @return
	 */
	public AABBTreeNode getAABBroot() {
		return root;
	}

}
