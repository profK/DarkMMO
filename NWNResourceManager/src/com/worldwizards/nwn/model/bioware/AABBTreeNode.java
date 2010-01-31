/**
 *
 * <p>Title: AABBNode.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004 Sun Microsystems, Inc.</p>
 * <p>Company: Sun Microsystems, Inc</p>
 * @author Jeff Kesselman
 * @version 1.0
 */
package com.worldwizards.nwn.model.bioware;

/**
 *
 * <p>Title: AABBNode.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004 Sun Microsystems, Inc.</p>
 * <p>Company: Sun Microsystems, Inc</p>
 * @author Jeff Kesselman
 * @version 1.0
 */
public class AABBTreeNode {
	public enum PLANE {PositiveX,PositiveY,PositiveZ,NegativeX,NegativeY,NegativeZ};
	float minX,minY,minZ;
	float maxX,maxY,maxZ;
	AABBTreeNode leftNode=null,rightNode=null;
	int leafFaceID = -1;
	PLANE[] significantPlanes = null;
	
	public AABBTreeNode(float minX,float minY, float minZ,
					 float maxX,float maxY, float maxZ,
					 PLANE[] significantPlanes){
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		this.significantPlanes = significantPlanes;
	}
	
	public void setLeftNode(AABBTreeNode l){
		leftNode = l;
	}
	
	public void setRightNode(AABBTreeNode r){
		rightNode = r;
	}
	
	public void setLeafFace(int ID){
		leafFaceID = ID;
	}

	/**
	 * @return
	 */
	public float[] getMax() {		
		return new float[]{maxX,maxY,maxZ};
	}
	
	public float[] getMin() {		
		return new float[]{minX,minY,minZ};
	}

	/**
	 * @return
	 */
	public int getFaceIdx() {		
		return leafFaceID;
	}

	/**
	 * @return
	 */
	public AABBTreeNode getLeft() {		
		return leftNode;
	}
	
	public AABBTreeNode getRight() {		
		return rightNode;
	}
}
