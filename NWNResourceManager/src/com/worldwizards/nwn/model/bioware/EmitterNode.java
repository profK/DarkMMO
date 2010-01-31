package com.worldwizards.nwn.model.bioware;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;


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
 * 
 * From the ASCII MDL EMitter docs Properties:
 * 
 * parent [node_name] - the parent node in the tree. All nodes have this
 * property. Top level nodes use NULL.
 * 
 * The following three properties are optional:
 * 
 * position [X] [Y] [Z] - The position in 3d space of this node.
 * 
 * orientation [X-Axis] [Y-Axis] [Z-Axis] [rotation_ammount] - the node is
 * rotated around the axis given in x,y,z
 * 
 * wirecolor [red] [green] [blue] - This probably defines the color for the
 * object's wireframe.
 * 
 * (there is a lot of speculation in the following, more testing is needed)
 * colorStart [red] [green] [blue] - This is probably the color a particle is at
 * birth. colorEnd [red] [green] [blue] - This is probably the color a particle
 * is at death, color values may be interpolated between these colors ovr the
 * course of the particle's life
 * 
 * alphaStart [float] - This probably controls the transparency of the particle
 * at birth. alphaEnd [float] - This probably controls the transparency of the
 * particle at death.
 * 
 * sizeStart [float] - This probably controls the size of the particle at birth.
 * sizeEnd [float] - This probably controls the size of the particle at death.
 * 
 * sizeStart_y [float] - Unknown. At a guess I would say the size properties
 * above provide uniform scale for the particle and these allow streching in the
 * y direction. This suggests properties might exist for x or z as well. Or this
 * could control something else entirely. (like the size of the control gizmo).
 * sizeEnd_y [float]
 * 
 * frameStart [int] - My guess is that this controls the frame of the animation
 * at which to begin producing particles. frameEnd [int] - The frame to stop
 * producing particles. These may combine with the fps property below.
 * 
 * birthrate [int] - A control on how fast to spawn new particles.
 * 
 * spawnType [int] - Unknown.
 * 
 * lifeExp [float] - The length of time a particle will "live". When combined
 * with birth rate this controls the number of particles in existance at a time.
 * 
 * mass [float] - The mass of a particle. (used for calculating the effects of
 * forces)
 * 
 * spread [float] - Probably the ammount that the particles spread out from each
 * other once they leave the emitter.
 * 
 * particleRot [float] - Probably the ammount that the particles rotate as they
 * move.
 * 
 * velocity [float] - Probably the initial velocity of a particle leaving the
 * emitter.
 * 
 * randvel [float] - Probably a control on how random the initial velocity of a
 * particle is.
 * 
 * fps [int] - My guess is that this works with frame start and frame end to
 * match the time frame of the emitter with the animation. In that case this
 * would be the number of frames per second. So an emitter that started on frame
 * 30 with fps of 10 might start in the 3rd second of the animation. This is
 * just speculation however.
 * 
 * random [1|0] - Unknown.
 * 
 * inherit [1|0] - Unknown. inherit_local [1|0] - Unknown. inherit_part [1|0] -
 * Unknown. inheritvel [1|0] - Unknown. (inherit velocity)
 * 
 * xsize [int??] - Unknown. (might relate to texture coordinates) ysize [int??] -
 * Unknown. (might relate to texture coordinates)
 * 
 * bounce [1|0] - Unknown. Possibly controls some sort of collision detection.
 * bounce_co [float] - Unknown. Could control elasticity of the bounce, or maybe
 * friction.
 * 
 * loop [int?? or 1|0??] - Unknown. Could specify that the emitter loops,
 * possibly a given number of times.
 * 
 * update [Fountain] - Unknown. Could be that there are different emitter types,
 * such as fountains or clouds.
 * 
 * render [Normal | Linked | Motion_blur] - Unknown. Probably controls how the
 * particles are drawn in some way.
 * 
 * Blend [Normal | Lighten] - Unknown.
 * 
 * update_sel [1|0] - Unknown.
 * 
 * render_sel [1|0] - Unknown.
 * 
 * blend_sel [1|0] - Unknown.
 * 
 * deadspace [float] - Unknown.
 * 
 * opacity [float] - Unknown. Maybe a global control on transparency for the
 * whole effect?
 * 
 * blurlength [float] - Unknown. Could control blur ammount.
 * 
 * lightningDelay [float] - Unknown.
 * 
 * lightningRadius [float] - Unknown.
 * 
 * lightningScale [float] - Unknown.
 * 
 * blastRadius [float] - Unknown.
 * 
 * blastLength [float] - Unknown.
 * 
 * twosidedtex [1|0] - Controls if the particles have textures on both sides of
 * them.
 * 
 * p2p [1|0] - Unknown.
 * 
 * p2p_sel [1|0] - Unknown.
 * 
 * p2p_type [Bezier|Gravity] - Unknown.
 * 
 * p2p_bezier2 [float] - Unknown.
 * 
 * p2p_bezier3 [float] - Unknown.
 * 
 * combinetime [float] - Unknown.
 * 
 * drag [float] - Ammount of drag on each particle.
 * 
 * grav [float] - Strength of gravity.
 * 
 * threshold [float] - Unknown.
 * 
 * texture [filename] - Texture for the particles.
 * 
 * xgrid [int] - Unknown. (might relate to texture coordinates)
 * 
 * ygrid [int] - Unknown. (might relate to texture coordinates)
 * 
 * affectedByWind [true|false] - Unknown. Probably controls how wind effects the
 * particles. What controls the wind is an interesting question.
 * 
 * m_isTinted [1|0] - Possibly controls if the particles are tinted by their
 * color or only by their texture. That's just a guess though.
 * 
 * renderorder [int??] - Unknown.
 * 
 * Splat [1|0] - Unknown.
 */

public class EmitterNode extends MdlNode {

	float deadSpace;

	float blastRadius;

	float blastLength;

	int xGrid;

	int yGrid;

	int spaceType;

	String updateFunc;

	String renderFunc;

	String blendFunc;

	String textureName;

	String chunkName;

	int twoSidedTexture;

	int loopFlag;

	short renderOrder;

	int emitterFlags;

	static enum EMITTERFLAG {
		P2P, P2PSel, WindEffects, IsTinted, Bounce, Random, Inherit, InheritVelocity, InheritLocal, Splat, InheritPart
	};

	/**
	 * fileDataSize
	 * 
	 * @return int
	 */
	public int fileDataSize() {
		return 0x0148;
	}

	/**
	 * EmitterNode
	 * 
	 * @param pos
	 *            int
	 * @param modelDataBuff
	 *            ByteBuffer
	 * @param rawDataBuff
	 *            ByteBuffer
	 */
	public EmitterNode(int pos, ByteBuffer modelDataBuff,
			ByteBuffer rawDataBuff, boolean trace) {
		super(pos, modelDataBuff, rawDataBuff, trace);
		if (trace) {
			System.out.println("Emitter Node: " + name);
		}
		deadSpace = modelDataBuff.getFloat();
		blastRadius = modelDataBuff.getFloat();
		blastLength = modelDataBuff.getFloat();
		xGrid = modelDataBuff.getInt();
		yGrid = modelDataBuff.getInt();
		spaceType = modelDataBuff.getInt();
		byte[] inbytes = new byte[32];
		modelDataBuff.get(inbytes);
		updateFunc = new String(inbytes);
		modelDataBuff.get(inbytes);
		renderFunc = new String(inbytes);
		modelDataBuff.get(inbytes);
		blendFunc = new String(inbytes);
		inbytes = new byte[64];
		modelDataBuff.get(inbytes);
		textureName = new String(inbytes);
		inbytes = new byte[16];
		chunkName = new String(inbytes);
		twoSidedTexture = modelDataBuff.getInt();
		loopFlag = modelDataBuff.getInt();
		renderOrder = modelDataBuff.getShort();
		modelDataBuff.getShort(); // padding
		emitterFlags = modelDataBuff.getInt();
	}

	Set<EMITTERFLAG> emitterFlagSet() {
		Set<EMITTERFLAG> set = new HashSet<EMITTERFLAG>();
		for (EMITTERFLAG flag : EMITTERFLAG.values()) {
			int bit = 1 << flag.ordinal();
			if ((emitterFlags & bit) == bit) {
				set.add(flag);
			}
		}
		return set;
	}

	

}
