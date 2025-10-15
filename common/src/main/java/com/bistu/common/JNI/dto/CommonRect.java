package com.bistu.common.JNI.dto;

import java.io.Serializable;

public class CommonRect implements Comparable<CommonRect>, Serializable {
	private static final long serialVersionUID = 8980407572262615262L;

	public double left;
	public double top;
	public double right;
	public double bottom;

	public CommonRect() {
		left = 0;
		top = 0;
		right = 0;
		bottom = 0;
	}
	public CommonRect(double l,double t,double r,double b) {
		left	= l;
		top	=t;
		right	= r;
		bottom	= b;
	}

	public CommonRect(CommonRect rect) {
		left	= rect.left;
		top	= rect.top;
		right	= rect.right;
		bottom	= rect.bottom;
	}

	public void SetRectValue(double l, double t, double r, double b) {
		left = l;
		top = t;
		right = r;
		bottom = b;
	}

	public boolean contains(double x, double y) {
		return left < x && x < right && top < y && y < bottom;
	}

	public boolean containY(double y) {
		return top < y && y < bottom;
	}

	/**
	 * tell whether the two given rects are roughly equal.
	 * @param r1	rect
	 * @param r2	rect
	 * @return	true for roughly equal, false otherwise.
	 */
	public static boolean isRoughlyEqual(CommonRect r1, CommonRect r2) {
		return (isNear(r1.left, r2.left) && isNear(r1.right, r2.right) && isNear(r1.top, r2.top) && isNear(r1.bottom, r2.bottom));
	}

	private static boolean isNear(double x0, double x1) {
		return (int) x0 == (int) x1;
	}

	/**
	 * exchange left and right if left > right,
	 * exchange top and bottom if top > bottom.
	 */
	public void normalize() {
		if (left > right) {
			double t = left;
			left = right;
			right = t;
		}
		if (top > bottom) {
			double t = top;
			top = bottom;
			bottom = t;
		}
	}

	@Override
	public String toString() {
		return "Rect : Left " + Integer.toString((int)left) +
				",Top " + Integer.toString((int)top) +
				",right " + Integer.toString((int)right) +
				",bottom " + Integer.toString((int)bottom);
	}

	@Override
	public int compareTo(CommonRect another) {
		return (int) (this.top - another.top);
	}

	public void copyTo(CommonRect dest) {
		dest.left	= this.left;
		dest.top	= this.top;
		dest.right	= this.right;
		dest.bottom	= this.bottom;
	}
}
