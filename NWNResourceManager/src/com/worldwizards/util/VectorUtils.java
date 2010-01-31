package com.worldwizards.util;

public class VectorUtils {
	private VectorUtils() {
		// cannot be instantiated
	}

	public static float[] subtract(float[] p1, float[] p0) {
		return new float[]{p1[0]-p0[0],p1[1]-p0[1],p1[2]-p0[2]};
	}

	public static float[] cross(float[] v1, float[] v2) {
		return new float[]{
				(v1[1]*v2[2])-(v1[2]*v2[1]),
				(v1[2]*v2[0])-(v1[0]*v2[2]),
				(v1[0]*v2[1])-(v1[1]-v2[0])
		};
	}

	public static float[] normalize(float[] v) {
		return divide(v,length(v));
		
	}

	public static float[] divide(float[] v, float length) {
		return new float[] {
				v[0]/length,v[1]/length,v[2]/length
		};
	}

	public static float length(float[] v) {
		return (float) Math.sqrt((v[0]*v[0])+(v[1]*v[1])+(v[2]*v[2]));
	}

}
